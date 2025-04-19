# HotelHawk

HotelHawk is an advanced hotel price analysis and comparison platform built to simplify the search for affordable accommodations. It aggregates hotel listings from leading travel websites like **Hotels.ca**, **Booking.com**, and **makemytrip.com**, and delivers real-time, filtered, and ranked results — all with an intuitive UI.

> 🚀 This is a forked version of a collaborative project.  
> I contributed to frontend UI enhancements using React and Material-UI, implemented hotel card components, worked on search filtering logic, and integrated toast and form functionality with React-Hook-Form and React-Toastify.

---

## 📌 Overview

HotelHawk consists of two primary components:

- 🖥️ **Frontend:** Built with React.js + Material-UI for a responsive, interactive user experience
- ⚙️ **Backend:** Powered by Java + Spring Boot and intelligent data structures for crawling, filtering, and ranking hotels

---

## 🌟 Key Features

- 🔍 **Multi-site Hotel Aggregation:** Fetches listings using Selenium-based crawlers
- 🔧 **Custom Filters:** Sort hotels by price, rating, location, and more
- 🧠 **Autocomplete + Spell Check:** Intelligent search suggestions using Trie and AVL Tree
- 📍 **Nearby Hotels:** Geo-indexing to locate hotels near your selected city
- 📜 **Search History Tracking:** Keeps a record of your recent queries
- 🧩 **Pattern Matching with Regex:** Accurately extracts and validates pricing information

---

## 🧠 Data Structures & Algorithms

| Feature                       | Structures/Algorithms Used                  |
| ----------------------------- | ------------------------------------------- |
| Web Crawler                   | `ArrayList`, `Hashtable`                    |
| HTML Parsing                  | `ArrayList`, `HashMap`, `Hashtable`         |
| Inverted Index Search         | `Trie`, `HashMap`, `Array`                  |
| Page Ranking                  | `PriorityQueue (MaxHeap)`, `HashMap`        |
| Spell Check & Word Completion | `AVL Tree`, `Trie`, Edit Distance Algorithm |
| Regex Pattern Matching        | Regex for price extraction                  |
| Filters                       | `ArrayList` for criteria application        |

---

## 🛠️ Tech Stack

### 🔹 Frontend

- JavaScript / React.js
- Material-UI
- React-Hook-Form
- React-Toastify

### 🔸 Backend

- Java
- Spring Boot
- Selenium
- Maven

---

## 📁 Project Structure

```bash
HotelHawk/
├── Backend/
│   ├── src/main/java/com/HotelHawk/Spring/Application.java
│   └── ... Spring Boot backend files
├── Frontend/
│   ├── src/       # React source code
│   ├── public/    # Static assets
│   ├── package.json
│   └── ...
└── README.md
```

---

## 🚀 Getting Started

### 🔧 Clone the Repository

```bash
git clone https://github.com/meetbhavsar99/HotelHawk.git
cd HotelHawk
```

---

### ⚙️ Backend Setup

```bash
cd Backend
```

Run the app via:

```bash
./mvnw spring-boot:run
```

> Or, open `Application.java` in an IDE like IntelliJ or Eclipse and run it manually.

---

### 💻 Frontend Setup

```bash
cd ../Frontend
npm install
npm start
```

Access the app at:  
🔗 [http://localhost:3000](http://localhost:3000)

---

## 📚 Project Submission

This project was submitted for the **COMP-8547: Advanced Computing Concepts** course during **Winter 2024** at the **University of Windsor**.  
It was also selected and showcased at the **9th CS Demo Day**.

---

## 🙌 Acknowledgment

This project was developed collaboratively.

I contributed to:

- UI design for hotel listing cards and filters (React + MUI)
- Integration of React-Hook-Form and Toast notifications
- Enhancing filtering logic and component-level cleanup
- Codebase restructuring and readability improvements

---

## 📜 License

This project is licensed under the [MIT License](LICENSE).

---

## 📫 Contact

**Meet Bhavsar**  
📧 [meetbhavsar99@gmail.com](mailto:meetbhavsar99@gmail.com)  
💼 [LinkedIn](https://www.linkedin.com/in/meet-bhavsar-0059ba1b5/)
