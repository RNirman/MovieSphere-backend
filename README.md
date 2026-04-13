# 🎬 MovieSphere

**MovieSphere** is a high-performance, full-stack movie discovery and management platform. It allows users to browse trending movies, search the global TMDb database in real-time, and enables administrators to curate a local collection with rich metadata and trailers.

---

## 🚀 Features

### 🌍 Public Features
* **Dynamic Discovery:** Instantly view popular movies from TMDb on the home page.
* **Global Search:** Real-time search across the TMDb database via a secure backend proxy.
* **Hybrid Detail Views:** Seamlessly view details for both local database entries and external TMDb titles.
* **Responsive Design:** Fully optimized for mobile, tablet, and desktop using Tailwind CSS.

### 🛡️ Admin Features
* **Secure Authentication:** Protected admin panel using JWT/Basic Auth.
* **One-Click Import:** Search for a movie via API and save all details (poster, director, synopsis, trailer ID) directly to the MySQL database.
* **Full CRUD:** Manually add, edit, or delete movies from the local curated collection.

---

## 🛠️ Tech Stack

### Frontend
* **React.js** (Functional Components, Hooks)
* **Tailwind CSS** (Modern Styling)
* **Axios** (API Communication)
* **Lucide React** (Iconography)

### Backend
* **Java 17+**
* **Spring Boot 3.x**
* **Spring Security** (Role-Based Access Control)
* **Spring Data JPA** (Hibernate)
* **MySQL** (Relational Database)

---

## 🔑 Security Implementation

One of the core strengths of MovieSphere is its security architecture:

1.  **API Key Masking:** The TMDb API key is stored exclusively in environment variables on the backend. No sensitive keys are exposed to the client-side browser.
2.  **Request Proxying:** All external API requests are proxied through Spring Boot, allowing for rate limiting and controlled access.
3.  **Encrypted Credentials:** Admin passwords are hashed using BCrypt.

---

## 📦 Installation & Setup

### Prerequisites
* JDK 17 or higher
* Node.js & npm
* MySQL Server
* TMDb API Key

### 1. Backend Configuration
Create a `src/main/resources/application-local.properties` file:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moviesphere_db
spring.datasource.username=your_username
spring.datasource.password=your_password

tmdb.api.key=YOUR_TMDB_API_KEY
tmdb.api.base-url=[https://api.themoviedb.org/3/](https://api.themoviedb.org/3/)
