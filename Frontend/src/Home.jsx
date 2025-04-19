import NavBar from "./NavBar";
import Slider from "react-slick";
import { useForm, Controller } from "react-hook-form";
import { useState } from "react";
import axios from "axios";
import { useEffect } from "react";
import {
  Autocomplete,
  Box,
  CircularProgress,
  TextField,
  Typography,
} from "@mui/material";
import TrendingUpIcon from "@mui/icons-material/TrendingUp";
import { ArrowBackOutlined, ArrowForwardOutlined } from "@mui/icons-material";
import Select from "react-select";
import { useRef } from "react";
import { Bounce, ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import StarIcon from "@mui/icons-material/Star";
import LocalOfferIcon from "@mui/icons-material/LocalOffer";
import { Collapse } from "react-bootstrap";
import dayjs from "dayjs";
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";

const Home = () => {
  const { register, handleSubmit, reset, control } = useForm();
  const {
    register: filterRegister,
    formState: { errors: filterErrors },
    handleSubmit: handleFilterSubmit,
    reset: filterReset,
    control: filterControl,
  } = useForm({});
  const [hotelData, setHotelData] = useState();
  const [spellCheck, setSpellCheck] = useState("");
  const [loader, setLoader] = useState(false);
  const [selectedOption, setSelectedOption] = useState("hotelsca");
  const [searchData, setSearchData] = useState("");
  const [pollingData, setPollingData] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [trendingCities, setTrendingCities] = useState({});
  const [selectedCrawling, setSelectedCrawling] = useState({
    value: "oldsearch",
    label: "Old Crawling",
  });
  const [hotelOptions, setHotelOptions] = useState([]);
  const [openFilter, setFilterOpen] = useState(false);
  const [cityName, setCityName] = useState("");
  const [rating, setRating] = useState("");
  const [hotelFreq, setHotelFreq] = useState([]);
  const hint = useRef("");
  const [isFocused, setIsFocused] = useState(false);

  // Filters
  const onFilterSubmit = (data) => {
    // Handle form submission here
    console.log(data, "filtersData");
    let ratingNumber;

    if (rating === "Good") {
      ratingNumber = 5;
    } else if (rating === "Average") {
      ratingNumber = 7;
    } else if (rating === "Excellent") {
      ratingNumber = 8;
    } else {
      ratingNumber = 0;
    }

    getFiltersData(cityName, data.minPrice, data.maxPrice, ratingNumber);
  };

  const clearFilters = () => {
    filterReset();
    setRating("");
  };

  console.log(hotelOptions, "hotelOptions");

  const getFiltersData = async (city, minPrice, maxPrice, rating) => {
    try {
      setLoader(true);
      const response = await axios.get(
        `http://localhost:8080/filter?cityname=${city}&minprice=${minPrice}&maxprice=${maxPrice}&minreviews=${rating}`
      );
      setHotelData(response.data);
      // filterReset()
      setLoader(false);
      setFilterOpen(false);
    } catch (err) {
      if (err.response.status === 400) {
        // setError(response.data);
        toast.error(err.response.data, {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
          transition: Bounce,
        });
      }
      console.log("Error getting filtered data: ", err);
      setLoader(false);
    }
  };

  const getHotelsData = async (city, checkin, checkout, hotel) => {
    try {
      const url =
        selectedCrawling.value === "oldsearch"
          ? `http://localhost:8080/datavalidate?crawl_type=oldsearch&cityname=${city}&checkin_date=${checkin}&checkout_date=${checkout}`
          : `http://localhost:8080/datavalidate?crawl_type=newsearch/&cityname=${city}&checkin_date=${checkin}&checkout_date=${checkout}`;

      const searchSelectUrl = `http://localhost:8080/select/${city}/${hotel}`;

      setLoader(true);
      const response = await axios.get(
        hotel?.length > 0 ? searchSelectUrl : url
      );

      if (hotel?.length > 0) {
        setHotelData({
          booking: response.data,
          hotelsca: response.data,
          mmt: response.data,
        });
      } else {
        setHotelData(response.data);
      }

      spellCheckApi(city);
      console.log(response.status, "status");
      // reset();

      setLoader(false);
    } catch (error) {
      if (error.response.status === 400) {
        // setError(response.data);
        toast.error(error.response.data, {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
          transition: Bounce,
        });
      }
      // reset();
      setLoader(false);
      console.error("Error fetching hotels data:", error);
    } finally {
      setLoader(false);
    }
  };

  const getTrendingCities = async (city) => {
    try {
      const response = await axios.get(`http://localhost:8080/pg/`);

      setTrendingCities(response?.data ? response.data : {});
    } catch (err) {
      console.log("Error fetching trending cities: ", err);
    }
  };

  const getFrequencyCounter = async (city) => {
    try {
      console.log(city, "freqCity");
      const response = await axios.get(`http://localhost:8080/fc/${city}`);
      console.log(response.data, "resFreq");
      setHotelFreq([response.data]);
    } catch (err) {
      console.log(err);
    }
  };

  const spellCheckApi = async (city) => {
    try {
      const response = await axios.get(
        `http://localhost:8080/spellcheck?cityname=${city}`
      );

      if (response.data.toLowerCase() !== city.toLowerCase()) {
        setSpellCheck(response.data);
        getFrequencyCounter(city);
        setCityName(city);
      } else {
        getFrequencyCounter(response.data);
        setCityName(response.data);
      }

      console.log(response.data, "resSpell");
    } catch (err) {
      if (err.response.status === 400) {
        // setError(response.data);
        toast.error(err.response.data, {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
          transition: Bounce,
        });
      }
      // reset();
      setLoader(false);
      console.error("Error fetching hotels data:", error);
      console.log("Error calling spellCheck api: ", err);
    }
  };

  const getSearchData = async () => {
    if (searchData !== "") {
      try {
        setIsLoading(true);
        const response = await axios.get(
          `http://localhost:8080/find/${searchData}`
        );
        setPollingData(response.data);

        setError(null);
        setIsLoading(false);
        return response;
      } catch (error) {
        console.log("Error fetching search data: ", error);
      } finally {
        if (pollingData.length > 0) {
          let tempFilteredHotels = [];

          pollingData.forEach((item) => {
            if (item?.Hotels?.length) {
              item.Hotels.split(",").forEach((hotel) => {
                tempFilteredHotels.push({
                  label: hotel.trim(), // trim to remove any leading/trailing spaces
                  value: `hotel/${item.Cityname}`,
                });
              });
            }
          });

          console.log(tempFilteredHotels, "tempHotels");

          setHotelOptions(tempFilteredHotels);
        }
      }
    }
    setHotelOptions([]);
    setPollingData([]);
  };

  useEffect(() => {
    if (searchData === "") {
      setSpellCheck("");
    }
    getSearchData();
    getTrendingCities();
    console.log(pollingData, "pol");
  }, [searchData]);

  const handleRegistration = (data) => {
    console.log(
      {
        ...data,
        checkin: formatValue(data.checkin),
        checkout: formatValue(data.checkout),
      },
      "searchData"
    );
    console.log(cityName, "cityname");
    getHotelsData(
      data.city,
      formatValue(data.checkin),
      formatValue(data.checkout)
    );
  };

  const handleWebsiteSelection = (option) => {
    setSelectedOption(option);
  };

  // Function to format the date as YYYY-MM-DD
  function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  }

  function addOneDay(date) {
    const newDate = new Date(date);
    newDate.setDate(newDate.getDate() + 1);
    return newDate;
  }

  // Get the current date
  const currentDate = new Date();
  const tomorrowDate = addOneDay(currentDate);

  // Format the current date
  const formattedCurrentDate = formatDate(currentDate);
  const formattedNextDate = formatDate(tomorrowDate);

  // react-slick settings
  const hotelListingsettings = {
    dots: true,
    arrows: false,
    autoplay: true,
    autoplaySpeed: 3000,
    infinite: true,
    speed: 2000,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  const CustomPrevArrow = (props) => {
    const { className, style, onClick } = props;
    return (
      <ArrowBackOutlined
        className={className}
        style={{
          ...style,
          display: "block",
          left: -40,
          top: 210,
          color: "#0877A2",
        }}
        onClick={onClick}
        fontSize="large"
      />
    );
  };

  const CustomNextArrow = (props) => {
    const { className, style, onClick } = props;
    return (
      <ArrowForwardOutlined
        className={className}
        style={{
          ...style,
          display: "block",
          right: -40,
          top: 210,
          color: "0877A2",
        }}
        onClick={onClick}
        fontSize="large"
      />
    );
  };

  const popularDestinationSettings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 4,
    slidesToScroll: 1,
    prevArrow: <CustomPrevArrow />,
    nextArrow: <CustomNextArrow />,
  };

  const formatValue = (date) => {
    return dayjs(date).format("YYYY-MM-DD");
  };

  const isLowerCase = (str) =>
    str.slice(str.length - 1, str.length) ===
    str.slice(str.length - 1, str.length).toLowerCase();

  console.log(hotelData, "hotelData");
  console.log(hotelFreq, "hotelFreq");

  return (
    <div className="container-xxl bg-white p-0">
      {/* Navbar Start */}
      <NavBar />
      {/* Navbar End */}
      {/* Header Start */}
      <div className="container-fluid header bg-white p-0 m-0" id="slogan">
        <ToastContainer
          position="top-right"
          autoClose={5000}
          hideProgressBar={false}
          newestOnTop={false}
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
          theme="dark"
          transition={Bounce}
        />
        <div className="row g-0 align-items-center flex-column-reverse flex-md-row">
          <h1 className="display-5 animated fadeIn mb-4 mt-5 text-white text-above-image line">
            <span className="text-primary">Comp</span>are Hotel Prices.<br></br>
            <span className="secondLine">
              Find Your <span className="text-primary">Perfect Stay.</span>
            </span>
          </h1>
          <img src="img/bg_slogan.png" alt="" />
          {/* <div className="col-md-6 mt-lg-3"></div> */}
          <div className="col-md-6 animated fadeIn">
            <div className="owl-carousel header-carousel">
              <div className="owl-carousel-item">
                <img className="img-fluid" src="img/carousel-1.jpg" alt="" />
              </div>
              <div className="owl-carousel-item">
                <img className="img-fluid" src="img/carousel-2.jpg" alt="" />
              </div>
            </div>
          </div>
        </div>
      </div>
      <div
        className="container mb-5 mt-5"
        style={{
          display: "flex",
          justifyContent: "center",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <h2 className="text-center mb-5 display-5 text-primary">
          Popular Destinations
        </h2>
        {/* <div className="row"> */}
        <div
          className="row"
          style={{
            paddingLeft: "50px",
            paddingRight: "50px",
            width: "100%",
          }}
        >
          <div className="popular-slick">
            <Slider {...popularDestinationSettings}>
              {trendingCities &&
                Object.keys(trendingCities).map((key, i) => (
                  <div key={key}>
                    <div
                      className="position-relative"
                      onClick={() => {
                        console.log(key, "key");
                        setCityName(key);
                        spellCheckApi(key);
                        getHotelsData(
                          key,
                          formattedCurrentDate,
                          formattedNextDate
                        );
                      }}
                    >
                      <a href="#hello-hotels">
                        <img
                          src={trendingCities[key].split(" ")[1]}
                          style={{
                            height: "400px",
                            width: "400px",
                            border: "1px solid transparent",
                            borderRadius: "10px",
                          }}
                          alt={"Hotel " + (i + 1)}
                          className="img-fluid"
                        />
                      </a>
                    </div>
                    <div className="container mt-3">
                      <div className="row">
                        <div
                          className="col text-start text-dark fw-bold"
                          style={{ fontSize: "1.2em" }}
                        >
                          {key}
                        </div>
                        <div className="col text-end">
                          <div
                            className="border-bottom rounded-2 d-inline-flex align-items-center shadow-sm mb-2"
                            style={{
                              backgroundColor: "#0877a2",
                              color: "white",
                            }}
                          >
                            <span className="p-2">
                              {trendingCities[key].split(" ")[0]}
                            </span>
                            <span
                              className="ms-1 me-2"
                              style={{ marginTop: "-1px" }}
                            >
                              <TrendingUpIcon />
                            </span>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
            </Slider>
          </div>
        </div>
      </div>
      <div
        className="container-fluid bg-primary mb-0 wow fadeIn"
        data-wow-delay="0.1s"
        style={{ padding: 35 }}
      >
        <div className="container">
          <form onSubmit={handleSubmit(handleRegistration)}>
            <div
              className="row"
              style={{
                display: "flex",
                flexDirection: "row",
                justifyContent: "center",
              }}
            >
              <div className="col-md-10">
                <div className="row g-4">
                  <div style={{ width: "30%" }}>
                    <Autocomplete
                      onKeyDown={(event) => {
                        if (event.key === "Tab") {
                          if (hint.current) {
                            setSearchData(hint.current);
                            event.preventDefault();
                          }
                        }
                      }}
                      disableClearable
                      onBlur={() => {
                        hint.current = "";
                      }}
                      disablePortal
                      forcePopupIcon={false}
                      inputValue={searchData}
                      onChange={(e, v) => {
                        console.log(v, "v");
                        setSearchData(v?.label ?? "");
                        hint.current = "";
                        getHotelsData(
                          v?.value.split("/")[1],
                          null,
                          null,
                          v?.label
                        );
                      }}
                      getOptionLabel={(option) => option?.label}
                      filterOptions={(options, state) => {
                        const displayOptions = options.filter((option) =>
                          option.value
                            .toLowerCase()
                            .trim()
                            .includes(state.inputValue.toLowerCase().trim())
                        );
                        return displayOptions;
                      }}
                      options={hotelOptions}
                      renderInput={(params) => {
                        return (
                          <Box
                            sx={{
                              position: "relative",
                              background: "white",
                              borderRadius: "4px",
                            }}
                          >
                            <Typography
                              sx={{
                                position: "absolute",
                                opacity: 0.5,
                                left: 14,
                                top: 16,
                                color: "CaptionText",
                              }}
                            >
                              {hint.current}
                            </Typography>
                            <TextField
                              {...params}
                              {...register("city")}
                              placeholder="Search City"
                              onChange={(e) => {
                                const newValue = e.target.value;
                                setSearchData(newValue);
                                const matchingOption = pollingData.find(
                                  (option) => {
                                    if (newValue.toLowerCase() === newValue) {
                                      return option.Cityname.toLowerCase().startsWith(
                                        newValue.toLowerCase()
                                      );
                                    } else {
                                      return option.Cityname.toUpperCase().startsWith(
                                        newValue.toUpperCase()
                                      );
                                    }
                                  }
                                );

                                if (newValue && matchingOption) {
                                  hint.current =
                                    newValue.toLowerCase() === newValue
                                      ? matchingOption.Cityname.toLowerCase()
                                      : matchingOption.Cityname.toUpperCase();
                                } else {
                                  hint.current = "";
                                }
                              }}
                              InputProps={{
                                ...params.InputProps,
                                endAdornment: (
                                  <>
                                    {isLoading && (
                                      <CircularProgress
                                        color="inherit"
                                        size={20}
                                      />
                                    )}
                                    {params.InputProps.endAdornment}
                                    {searchData !== "" &&
                                      pollingData.length > 0 && (
                                        <>
                                          <TrendingUpIcon
                                            style={{ color: "#0877A2" }}
                                            fontSize="small"
                                          />
                                          <span>
                                            {pollingData[0].Search_Freq}
                                          </span>
                                        </>
                                      )}
                                  </>
                                ),
                              }}
                            />
                          </Box>
                        );
                      }}
                    />
                  </div>
                  <div style={{ width: "25%", marginLeft: "15px" }}>
                    <Controller
                      name="checkin"
                      control={control}
                      {...register("checkin")}
                      defaultValue={null}
                      render={({ field }) => (
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                          <DatePicker
                            {...field}
                            disablePast
                            shouldDisableDate={(day) => {
                              const today = dayjs();

                              // Disable today
                              if (dayjs(day).isSame(today, "day")) {
                                return true;
                              }

                              return false;
                            }}
                            // fullWidth
                            label={"Check In"}
                            inputVariant="outlined"
                            format="DD/MM/YYYY"
                            sx={{
                              background: "white",
                              borderRadius: "5px",
                            }}
                          />
                        </LocalizationProvider>
                      )}
                    />
                  </div>
                  <div style={{ width: "25%" }}>
                    <Controller
                      name="checkout"
                      control={control}
                      {...register("checkout")}
                      defaultValue={null}
                      render={({ field }) => (
                        <LocalizationProvider dateAdapter={AdapterDayjs}>
                          <DatePicker
                            {...field}
                            disablePast
                            // fullWidth
                            label={"Check Out"}
                            inputVariant="outlined"
                            format="DD/MM/YYYY"
                            shouldDisableDate={(day) => {
                              const today = dayjs();

                              // Disable today
                              if (dayjs(day).isSame(today, "day")) {
                                return true;
                              }

                              return false;
                            }}
                            sx={{
                              background: "white",
                              borderRadius: "5px",
                            }}
                          />
                        </LocalizationProvider>
                      )}
                    />
                  </div>
                  <div style={{ width: "18%" }}>
                    <Select
                      {...register("crawling", {
                        value: selectedCrawling.value,
                      }).name}
                      defaultValue={selectedCrawling}
                      options={[
                        { value: "oldsearch", label: "Old Crawling" },
                        { value: "newsearch", label: "New Crawling" },
                      ]}
                      onChange={(value) => {
                        setSelectedCrawling(value);
                      }}
                      isSearchable={false}
                      styles={{
                        control: (provided) => ({
                          ...provided,
                          border: "none",
                          borderRadius: "4px",
                          minHeight: "55px",
                        }),
                        indicatorSeparator: () => ({
                          display: "none",
                        }),
                        placeholder: (provided) => ({
                          ...provided,
                          color: "#666565",
                        }),
                      }}
                    />
                  </div>
                </div>
                {searchData !== "" && spellCheck !== "" && (
                  <div style={{ marginTop: "10px" }}>
                    <span style={{ color: "white" }}>
                      Did you mean: {spellCheck}
                    </span>
                  </div>
                )}
              </div>
              <div className="col-lg-1 col-md-2">
                <button className="button btn btn-dark border-0 w-100 py-3">
                  Search
                </button>
              </div>
            </div>
          </form>
        </div>
        <div id="hello-hotels"></div>
      </div>
      {/* Search End */}

      {/* Property List Start */}
      <div className="container-xxl py-5">
        <div className="container">
          <div className="row g-0 gx-5 align-items-end">
            <div className="col-lg-6">
              <div
                className="text-start mx-auto mb-5 wow slideInLeft"
                data-wow-delay="0.1s"
              >
                <h1 className="mb-3 text-primary">
                  Hotel Listing {!loader && cityName !== "" && "- " + cityName}
                </h1>
              </div>
            </div>
            <div
              className="col-lg-6 text-start text-lg-end wow slideInRight"
              data-wow-delay="0.1s"
            >
              <ul className="nav nav-pills d-inline-flex justify-content-end mb-5">
                <li className="nav-item me-2">
                  <a
                    className="btn btn-outline-primary active"
                    data-bs-toggle="pill"
                    href="#tab-1"
                    onClick={() => handleWebsiteSelection("hotelsca")}
                  >
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                      }}
                    >
                      <span>Hotels.ca</span>
                      {!loader && hotelFreq?.length > 0 && (
                        <div
                          style={{
                            width: "25px",
                            height: "25px",
                            backgroundColor: "#3498db",
                            borderRadius: "50%",
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            color: "white",
                            fontSize: "15px",
                            marginLeft: "10px",
                          }}
                        >
                          <div>{hotelFreq[0].hotelsca}</div>
                        </div>
                      )}
                    </div>
                  </a>
                </li>
                <li className="nav-item me-2">
                  <a
                    className="btn btn-outline-primary"
                    data-bs-toggle="pill"
                    href="#tab-2"
                    onClick={() => handleWebsiteSelection("booking")}
                  >
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                      }}
                    >
                      <span>Bookings.ca</span>
                      {!loader && hotelFreq?.length > 0 && (
                        <div
                          style={{
                            width: "25px",
                            height: "25px",
                            backgroundColor: "#3498db",
                            borderRadius: "50%",
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            color: "white",
                            fontSize: "15px",
                            marginLeft: "10px",
                          }}
                        >
                          <div>{hotelFreq[0].booking}</div>
                        </div>
                      )}
                    </div>
                  </a>
                </li>
                <li className="nav-item me-0">
                  <a
                    className="btn btn-outline-primary"
                    data-bs-toggle="pill"
                    href="#tab-3"
                    onClick={() => handleWebsiteSelection("mmt")}
                  >
                    <div
                      style={{
                        display: "flex",
                        justifyContent: "space-between",
                      }}
                    >
                      <span>MakeMyTrip.com</span>
                      {!loader && hotelFreq?.length > 0 && (
                        <div
                          style={{
                            width: "25px",
                            height: "25px",
                            backgroundColor: "#3498db",
                            borderRadius: "50%",
                            display: "flex",
                            justifyContent: "center",
                            alignItems: "center",
                            color: "white",
                            fontSize: "15px",
                            marginLeft: "10px",
                          }}
                        >
                          <div>{hotelFreq[0].mmt}</div>
                        </div>
                      )}
                    </div>
                  </a>
                </li>
              </ul>
            </div>
          </div>
          {!loader && hotelData && (
            <div className="row g-0 gx-5 align-items-end">
              <div className="col-lg-6">
                <div className="mb-5 container-filter bg-white">
                  <div className="accordion" id="filterAccordion">
                    <div className="accordion-item">
                      <h2 className="accordion-header" id="filterHeading">
                        <button
                          className={`${
                            !openFilter
                              ? "accordion-button collapsed"
                              : "accordion-button"
                          } text-dark fw-bold fs-5`}
                          onClick={() => setFilterOpen(!openFilter)}
                          aria-controls="example-collapse-text"
                          aria-expanded={openFilter}
                          type="button"
                          data-bs-toggle="collapse"
                          data-bs-target="#filterCollapse"
                        >
                          Filters
                        </button>
                      </h2>
                      <Collapse in={openFilter}>
                        <div
                          id="example-collapse-text"
                          className="accordion-collapse collapse"
                          aria-labelledby="filterHeading"
                          data-bs-parent="#filterAccordion"
                        >
                          <div className="accordion-body">
                            <form onSubmit={handleFilterSubmit(onFilterSubmit)}>
                              <div className="mb-3 d-flex align-items-center">
                                <span className="icon-wrapper text-primary">
                                  <LocalOfferIcon className="mt-0 icon-sm" />
                                </span>
                                <label className="form-label ms-2 me-4 my-auto text-primary fs-5">
                                  Price:
                                </label>
                                <div className="row">
                                  <div className="col-5">
                                    <div className="input-group">
                                      <span className="input-group-text">
                                        Min <br />
                                        (CAD)
                                      </span>
                                      <input
                                        type="number"
                                        {...filterRegister("minPrice")}
                                        className="form-control form-control-lg text-primary"
                                        aria-label="Minimum Price"
                                      />
                                    </div>
                                  </div>
                                  <span className="col-1 mx-0 align-self-center text-dark fw-bold fs-5 text-center">
                                    to
                                  </span>
                                  <div className="col-5">
                                    <div className="input-group">
                                      <span className="input-group-text">
                                        Max <br />
                                        (CAD)
                                      </span>
                                      <input
                                        type="number"
                                        {...filterRegister("maxPrice")}
                                        className="form-control form-control-lg text-primary"
                                        aria-label="Maximum Price"
                                      />
                                    </div>
                                  </div>
                                </div>
                              </div>
                              <div className="mb-3 d-flex align-items-center">
                                <span className="mt-0 pt-0 icon-wrapper text-primary">
                                  <StarIcon className="mt-0 pt-0 icon-sm" />
                                </span>
                                <label className="form-label ms-2 me-3 my-auto text-primary fs-5">
                                  Rating:
                                </label>
                                <div className="d-flex">
                                  <button
                                    type="button"
                                    className={`btn btn-outline-primary me-3 btn-f ${
                                      rating === "Average"
                                        ? "active btn-primary"
                                        : ""
                                    }`}
                                    onClick={() => setRating("Average")}
                                  >
                                    <span className="icon-wrapper me-1">
                                      <StarIcon className="mt-0 icon-sm" />
                                    </span>
                                    Average
                                  </button>
                                  <button
                                    type="button"
                                    className={`btn btn-outline-primary me-3 btn-f ${
                                      rating === "Good"
                                        ? "active btn-primary"
                                        : ""
                                    }`}
                                    onClick={() => setRating("Good")}
                                  >
                                    <span className="icon-wrapper me-1">
                                      <StarIcon className="mt-0 icon-sm" />
                                    </span>
                                    Good
                                  </button>
                                  <button
                                    type="button"
                                    className={`btn btn-outline-primary me-3 btn-f ${
                                      rating === "Excellent"
                                        ? "active btn-primary"
                                        : ""
                                    }`}
                                    onClick={() => setRating("Excellent")}
                                  >
                                    <span className="icon-wrapper me-1">
                                      <StarIcon className="mt-0 icon-sm" />
                                    </span>
                                    Excellent
                                  </button>
                                </div>
                              </div>
                              <div className="mb-3">
                                <button
                                  type="submit"
                                  className="btn btn-outline-primary btn-f"
                                >
                                  Apply Filters
                                </button>
                                <button
                                  type="button"
                                  className="btn btn-outline-primary ms-2 btn-f"
                                  onClick={clearFilters}
                                >
                                  Clear Filters
                                </button>
                              </div>
                            </form>
                          </div>
                        </div>
                      </Collapse>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          )}
          <div className="tab-content">
            <div id="tab-1" className="tab-pane fade show p-0 active">
              <div className="row g-4">
                {loader ? (
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      justifyContent: "center",
                      alignItems: "center",
                      gap: "20px",
                      marginBottom: "100px",
                      marginTop: "100px",
                    }}
                  >
                    <img width={"50px"} src="Spinner.gif" alt="spinner gif" />
                    <span style={{ fontSize: "20px" }} className="text-primary">
                      Please wait while we crawl fresh content for you.
                    </span>
                  </div>
                ) : hotelData?.[selectedOption]?.length > 0 ? (
                  [...hotelData?.[selectedOption]].reverse().map((item) => (
                    <div
                      className="col-lg-4 col-md-6 wow fadeInUp"
                      data-wow-delay="0.1s"
                    >
                      <div className="property-item">
                        <div className="">
                          <Slider {...hotelListingsettings}>
                            {item?.["Images "]
                              ?.trim()
                              .split(" ")
                              .map((photo) => (
                                <div>
                                  <img
                                    className="img-fluid"
                                    style={{
                                      width: "450px",
                                      height: "300px",
                                    }}
                                    src={photo}
                                    alt=""
                                  />
                                </div>
                              ))}
                          </Slider>
                        </div>
                        <div className="p-4 pb-0">
                          {item?.["Distance "]?.length > 0 && (
                            <h6 className="text-primary mb-3">
                              {Number(item?.["Distance "]).toFixed(2)} Km away
                            </h6>
                          )}
                          <h5 className="text-primary mb-3">
                            {item?.["MinPrice "] === "Not Available "
                              ? "Price not available"
                              : item?.["MinPrice "].split(" ")[0] === "CAD"
                              ? `$ ${Math.min(
                                  item?.["MinPrice "].split(" ")[1]
                                )}`
                              : `$ ${Math.min(
                                  item?.["MinPrice "].split(" ")[0]
                                )}`}
                          </h5>
                          <a className="d-block h5 mb-2" href="">
                            {item?.["Name "]}
                          </a>
                          <div
                            style={{
                              display: "flex",
                              gap: "10px",
                              paddingBottom: "10px",
                            }}
                          >
                            <img
                              width={"16px"}
                              src="rating.svg"
                              alt="ratings"
                            />
                            <span>
                              {item?.["Review "]?.split(" ")[0] &&
                              item?.["Review "]?.split(" ")[0] !== " "
                                ? item?.["Review "]?.split(" ")[0]
                                : "No reviews"}
                            </span>
                          </div>
                          {item?.["Facilities "] !== " " && (
                            <div
                              style={{
                                display: "flex",
                                gap: "10px",
                                paddingBottom: "10px",
                              }}
                            >
                              <img
                                width={"15px"}
                                src="facility.svg"
                                alt="ratings"
                              />
                              <div>
                                {item?.["Facilities "] !== " " &&
                                item?.["Facilities "]?.split(",")?.length >
                                  0 ? (
                                  item?.["Facilities "]
                                    ?.split(",")
                                    .slice(0, 3)
                                    .map((facility, index, array) => (
                                      <span>
                                        {facility.trim()}
                                        {index !== array.length - 1 && ","}
                                        &nbsp;
                                      </span>
                                    ))
                                ) : (
                                  <span>Data not available</span>
                                )}
                              </div>
                            </div>
                          )}
                          <div
                            style={{
                              display: "flex",
                              gap: "10px",
                              paddingBottom: "10px",
                            }}
                          >
                            <img
                              width={"15px"}
                              src="location.svg"
                              alt="ratings"
                            />
                            {item?.["Location "] === " " && (
                              <span>Data not available</span>
                            )}
                            {item?.["Location "] !== " " && (
                              <a
                                style={{
                                  textDecoration: "underline",
                                  cursor: "pointer",
                                  color: "#666565",
                                }}
                                target="_blank"
                                href={`https://google.com/maps/search/?api=1&query=${item?.[
                                  "Location "
                                ]?.trim()}`}
                                rel="noreferrer"
                              >
                                {item?.["Location "]?.trim()}
                              </a>
                            )}
                          </div>
                        </div>
                      </div>
                    </div>
                  ))
                ) : (
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      justifyContent: "center",
                      alignItems: "center",
                      gap: "20px",
                    }}
                  >
                    <img
                      style={{ width: "400px" }}
                      src="no-data-preview.svg"
                      alt="no-data-img"
                    />
                    <h4>No data available!</h4>
                    <h4>
                      Please use the search bar to find and compare hotel
                      prices.
                    </h4>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* about us container */}
      <div className="container-xxl py-5">
        <div className="container text-center py-3">
          <h1 className="display-5 text-primary">About Us</h1>
        </div>

        <div className="container py-5">
          <div className="row align-items-center">
            {/* <!-- Left Column for Picture --> */}
            <div className="col-md-6 image-column">
              <img
                src="img/aboutUs.png"
                className="img-fluid"
                alt="About Us Image"
              ></img>
            </div>
            {/* <!-- Right Column for Text --> */}
            <div className="col-md-6">
              <h2 className="display-6 text-secondary">
                Why<br></br>Choosing Us
              </h2>
              <br />
              <p className="fs-5 text-dark">
                We are not just a hotel booking platform; we're your trusted
                companion in unlocking the best stay experiences. At HotelHawk,
                our core commitment is to redefine your travel planning by
                providing unparalleled hotel price analysis. Our platform
                empowers you to effortlessly explore a wide range of hotel
                options, carefully analyzing prices, ratings, and essential
                details for your specified location. We take pride in our
                precision-driven approach, ensuring that you make informed
                decisions that align with your preferences and budget. With
                HotelHawk, your journey begins with a seamless, user-friendly
                interface, guiding you through a world of accommodation
                possibilities. Elevate your travel experience, embrace
                convenience, and choose HotelHawk for a smarter, more satisfying
                way to discover and book your ideal stay.
              </p>
            </div>
          </div>
        </div>
      </div>

      <div className="container-xxl py-5">
        <div className="container">
          <div
            className="text-center mx-auto mb-5 wow fadeInUp"
            data-wow-delay="0.1s"
            style={{ maxWidth: 600 }}
          >
            <h1 className="mb-3 text-primary">Our Team Members</h1>
          </div>
          <div className="container team-members-container row g-5">
            <div
              className="col-lg-2 col-md-4 col-sm-6 wow fadeInUp team-item"
              data-wow-delay="0.1s"
            >
              <div className="team-item overflow-hidden">
                <div className="position-relative">
                  <img className="img-fluid" src="img/heet.png" alt="" />
                </div>
                <div className="text-center p-4 mt-3">
                  <h5 className="fw-bold mb-0 text-primary">
                    Heet
                    <br />
                    Patel
                  </h5>
                </div>
              </div>
            </div>
            <div
              className="col-lg-2 col-md-4 col-sm-6 wow fadeInUp team-item"
              data-wow-delay="0.3s"
            >
              <div className="team-item overflow-hidden">
                <div className="position-relative">
                  <img className="img-fluid" src="img/shrey.png" alt="" />
                </div>
                <div className="text-center p-4 mt-3">
                  <h5 className="fw-bold mb-0 text-primary">Shrey Shah</h5>
                </div>
              </div>
            </div>
            <div
              className="col-lg-2 col-md-4 col-sm-6 wow fadeInUp team-item"
              data-wow-delay="0.5s"
            >
              <div className="team-item overflow-hidden">
                <div className="position-relative">
                  <img className="img-fluid" src="img/mihir.png" alt="" />
                </div>
                <div className="text-center p-4 mt-3">
                  <h5 className="fw-bold mb-0 text-primary">Mihir Jadeja</h5>
                </div>
              </div>
            </div>
            <div
              className="col-lg-2 col-md-4 col-sm-6 wow fadeInUp team-item"
              data-wow-delay="0.7s"
            >
              <div className="team-item overflow-hidden">
                <div className="position-relative">
                  <img className="img-fluid" src="img/vrutik.png" alt="" />
                </div>
                <div className="text-center p-4 mt-3">
                  <h5 className="fw-bold mb-0 text-primary">Vrutik Parmar</h5>
                </div>
              </div>
            </div>
            <div
              className="col-lg-2 col-md-4 col-sm-6 wow fadeInUp team-item"
              data-wow-delay="0.9s"
            >
              <div className="team-item overflow-hidden">
                <div className="position-relative">
                  <img className="img-fluid" src="img/Meet.png" alt="" />
                </div>
                <div className="text-center p-4 mt-3">
                  <h5 className="fw-bold mb-0 text-primary">Meet Bhavsar</h5>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div
        className="container-fluid text-white-50 footer wow fadeIn mt-2 ml-0 mr-0 mb-2 px-0 mx-0"
        data-wow-delay="0.1s"
      >
        <div className="container px-0 mx-0">
          <div className="footer-image-container border-0 mt-5">
            <img
              src="img/footer_merged.svg"
              alt="Footer"
              className="footer-image"
            />
          </div>
          <div className="container-fluid p-0">
            <p className="copyright text-center border-0">
              Copyright © 2024 HotelHawk.com™. All rights reserved.
            </p>
          </div>
        </div>
      </div>

      <a href="#" className="btn btn-lg btn-primary btn-lg-square back-to-top">
        <i className="bi bi-arrow-up" />
      </a>
    </div>
  );
};

export default Home;
