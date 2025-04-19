const NavBar = () => {
  return (
    <div className="container-fluid nav-bar bg-transparent">
      <nav className="navbar navbar-expand-lg bg-white navbar-light py-0 px-4 sticky-top">
        <a
          href="index.html"
          className="navbar-brand d-flex align-items-center text-center"
        >
          <div className="icon p-2 me-2 icon_image">
            <img
              className="img-fluid"
              src="img/icon-deal.png"
              alt="Icon"
              style={{ width: 50, height: 50 }}
            />
          </div>
          <h1 className="m-0 text-primary display-4">HotelHawk</h1>
        </a>

        <div className="d-flex align-items-center ms-auto">
          <img
            className="img-fluid"
            src="img/logo_right.png"
            alt="Another Image"
            style={{
              width: 110,
              height: 70,
              paddingRight: 0,
              justifyContent: "space-between",
            }}
          />
        </div>
      </nav>
    </div>
  );
};

export default NavBar;
