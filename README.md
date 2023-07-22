---

# MLB GAME APP - Scala ZIO Application

GameApp is a Scala ZIO application that provides RESTful endpoints for managing games. It uses ZIO for asynchronous and concurrent programming and ZIO-HTTP for handling HTTP requests and responses.

## Prerequisites

- Scala (2.13.x or higher)
- SBT (1.5.x or higher)
- Java Development Kit (JDK 8 or higher)
- H2 Database (for development purposes)

## Getting Started

1. Clone the repository to your local machine:

```
git clone https://github.com/your_username/GameApp.git
cd GameApp
```

2. Ensure you have H2 Database installed and running.

3. Set up the H2 Database configuration:
   - Open the `application.conf` file located in `src/main/resources` directory.
   - If you want to use a longterm database modify the `db.url`, `db.user`, and `db.password` properties to connect to your H2 Database instance.

4. Build the project using SBT:

```
sbt compile
```

5. Run the application:

```
sbt run
```

The application should start, and you should see the server running at `http://localhost:8080`.

## Endpoints

The following endpoints are available:

1. **GET /games?page=1&limit=100**

   Retrieves a paginated list of games.

   - Parameters:
     - `page` (optional): The page number (default: 1).
     - `limit` (optional): The maximum number of games per page (default: 100).

2. **POST /games**

   Creates a new game.

   - Request Body: JSON object representing the game. Example:

   ```json
   {
     "season": 2023,
     "neutral": false,
     "playoff": null,
     "team1": "Team A",
     "team2": "Team B",
     "score1": 3.5,
     "score2": 2.0
   }
   ```

3. **GET /games/:id**

   Retrieves details of a specific game by its ID.

4. **POST /seed**

   Seeds the database with games. (For development purposes)

## JSON Format

Both request and response bodies should be in JSON format.

- Game JSON format:

```json
{
  "season": 2023,
  "neutral": false,
  "playoff": null,
  "team1": "Team A",
  "team2": "Team B",
  "score1": 3.5,
  "score2": 2.0
}
```

- GameOutput JSON format:

```json
{
  "uuid": "123e4567-e89b-12d3-a456-426655440000",
  "season": 2023,
  "neutral": false,
  "playoff": null,
  "team1": "Team A",
  "team2": "Team B",
  "score1": 3.5,
  "score2": 2.0
}
```

## Contributing

Contributions are welcome! Feel free to open a pull request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

Please note that the above README is just a template and may need to be modified based on your specific project structure, dependencies, and requirements. Make sure to update the README with accurate and relevant information about your project.
