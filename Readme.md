# HotelHawk

HotelHawk is an advanced hotel price analysis platform designed to simplify the process of finding the perfect accommodation. By crawling and aggregating data from leading booking websites like Hotels.ca, Booking.com, and makemytrip.com, HotelHawk allows users to seamlessly compare hotel prices, explore popular destinations, track recent searches, and locate nearby hotels with ease. The platform is built with a user-centric approach, offering features such as customizable filters for price and rating, spell-check functionality, and word completion, ensuring that users have a smooth and efficient experience.

## Project Overview

HotelHawk is divided into two primary components: the **Frontend** and the **Backend**. The project leverages a range of technologies and data structures to deliver high-performance and reliable results.

### Key Features

- **Hotel Data Aggregation**: HotelHawk collects and aggregates hotel data from multiple booking websites using selenium web crawler, allowing users to compare prices and features across different platforms.
- **Customizable Filters**: Users can apply filters based on price, rating, and other criteria to refine their hotel search results.
- **City Insights**: The platform provides insights into popular and trending cities, helping users discover new travel destinations.
- **Recent Search History**: HotelHawk tracks users' recent searches, making it easy to revisit previous searches.
- **Nearby Hotels**: The platform uses location-based indexing to help users find hotels near their chosen destination.
- **Spell Check and Word Completion**: These features improve search accuracy by correcting misspelled words and suggesting completions for partially typed words.
- **Pattern Finding using Regex**: This feature is used to accurately extract and display hotel prices from the aggregated data.

### Data Structures and Algorithms Used

The project makes use of various data structures and algorithms to ensure efficient data processing and retrieval:

- **Web Crawler**:

  - **Data Structures**: `ArrayList`, `Hashtable`
  - **Purpose**: To collect and organize data from multiple websites.

- **HTML Parser**:

  - **Data Structures**: `ArrayList`, `Hashtable`, `HashMap`
  - **Purpose**: To parse and extract meaningful information from HTML documents, such as hotel names, prices, ratings, and locations.

- **Inverted Indexing**:

  - **Data Structures**: `Trie`, `HashMap`, `Array`
  - **Purpose**: Enables efficient searching of documents by associating search terms with their locations in the data.

- **Page Ranking**:

  - **Data Structures**: `Priority Queue (MaxHeap)`, `HashMap`
  - **Purpose**: To rank and display the most relevant search results.

- **Spell Checking and Word Completion**:

  - **Data Structures**: `AVL Tree`, `Trie`
  - **Algorithms**: Edit Distance Algorithm
  - **Purpose**: To correct spelling errors and provide word suggestions.

- **Pattern Finding using Regex**:

  - **Libraries**: Regex
  - **Purpose**: To extract and validate numerical data such as prices from the text.

- **Filters**:
  - **Data Structures**: `ArrayList`
  - **Purpose**: To filter search results based on user-defined criteria.

## Technologies Used

- **Frontend**:

  - JavaScript
  - ReactJS
  - Material-UI
  - React-Toastify
  - React-Hook-Form

- **Backend**:
  - Java
  - Spring Boot
  - Selenium
  - Algorithms and Data Structures

## Getting Started

To get started with HotelHawk, follow the steps below:

### Clone the Repository

First, clone the project from GitHub:

```bash
git clone https://github.com/Vrutik21/HotelHawk.git
```

### Backend Setup

1. Navigate to the Backend directory:

```bash
cd Backend
```

2. Start the Backend by running the main application file located at `Backend/src/main/java/com/HotelHawk/Spring/Application.java`. You can do this using an IDE like VS code or Eclipse, or via the command line with the following command:

```bash
./mvnw spring-boot:run
```

Alternatively, if you are using an IDE, simply open the project, locate the Application.java file, and run it.

### Frontend Setup

1. Navigate to the Frontend directory:

```bash
cd Frontend
```

2. Install the required dependencies:

```bash
npm i
```

3. Start the Frontend server:

```bash
npm run start
```

4. Once the frontend is started, open your browser and navigate to `http://localhost:3000` to view the application.

## Project Structure

```bash
HotelHawk/
├── Backend/
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/com/HotelHawk/Spring/Application.java
│ │ └── ...
│ └── ...
├── Frontend/
│ ├── src/
│ ├── public/
│ ├── package.json
│ └── ...
└── README.md
```

- **Backend**:: Contains the Java Spring Boot application.
- **Frontend**: Contains the ReactJS application.

## Project Submission

### This project is submitted as part of the COMP-8547:Advanced Computing Concepts course for the Winter 2024 session. It involves demonstrating the project during a scheduled slot. The project was also selected at 9th CS demo day at University of Windsor.
