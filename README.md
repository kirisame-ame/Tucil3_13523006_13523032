# Rush Hour Puzzle Solver
A solver website for https://en.wikipedia.org/wiki/Rush_Hour_(puzzle)
## Deployed on https://rushsolver.kirisame.jp.net/
## Input .txt file format
```
6 6
11
AAB..F
..BCDF
GPPCDFK
GH.III
GHJ...
LLJMM.
```
- First Row is board size A x B
- Second is non Primary Piece count
- Primary Piece is denoted with P
- End Goal with K
- Moveable space with .
- And other pieces with other letters
- DO NOT INPUT THE WALL
## Architecture
Frontend is built using React + Vite, and Backend is build using Spring Boot
## Built with
- Java 24
- Maven (3.9.9)
- Node.js (22.11.0)
## Directory Structure
- ```frontend``` : The React frontend
- ```rush_solver``` : The Java Spring Boot backend
- ```tests``` : The test cases used
- ```docs``` : Course report document
## How to run
- run ```backend.cmd``` for the spring boot backend,
- ```npm run dev``` in /frontend to run the React Frontend
## Authors
| Name | NIM | Class |
|------|---|---|
| William Andrian Dharma T | 13523006 | K01 |  
| Nathan Jovial Hartono | 13523032 | K01 |
