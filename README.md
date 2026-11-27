# UserApi

## 📌 Enunciat del exercici
- Què és una API REST i com funciona.
- Com definir endpoints a través de controladors amb @RestController.
- Ús dels mètodes HTTP GET i POST per recuperar i enviar informació.
- Com rebre dades a través de la URL amb @PathVariable i @RequestParam.
- Com rebre dades JSON a través del cos de la petició amb @RequestBody.
- Com retornar respostes en format JSON.
- Com provar manualment la teva API amb Postman (eina per enviar peticions HTTP).
- Com provar automàticament la aplicació amb MockMvc, @SpringBootTest i usant Mockito
- Com compilar i executar el .jar generat amb Maven (Spring Boot inclou el servidor integrat Apache Tomcat).
- Què és el concepte d’Inversió de Control (IoC) i com es creen i injecten Beans.
- Introducció a l’arquitectura per capes, i als patrons Service Layer i Repository.

**Descripció**: Spring Boot i el desenvolupament d’una API REST. 

L’objectiu és construir una API mínima però funcional, que permeti rebre i retornar dades en format JSON, utilitzant mètodes HTTP i aplicant bones pràctiques des del primer moment.

## ✨ Funcionalitats

### USERS Endpoints

POST http://localhost:9000/users : Saves a user.

GET http://localhost:9000/users : Returns all users.

GET http://localhost:9000/users?name=ABC : Returns all users with name query parameter.

GET http://localhost:9000/users/:id : Returns all users with id path variable.

## 🛠 Tecnologies
- Backend: Java, Springboot

## 🚀 Instal·lació i Execució
1. Clonar el repositori: `git clone https://github.com/FeroXx07/UserApi.git`
2. Compilar l'aplicació, utilitzar maven wrapper `./mvnw` si maven no està install.
   1. `mvn clean package`
3. Execució de l'aplicació: 
   1. Maven: `mvn spring-boot:run`
   2. Java: `java -jar target/userapi-0.0.1-SNAPSHOT.jar`
4. Com executar les proves.
   1. Maven: `mvn test`
   2. Manualment des del IDE.