# bankingApi

Introduction - A simple banking api built in spring boot without a database except using a map for repository. Built in java 11 had to revert to java 8 before deploying to heroku.


Technologies 
* spring-boot
* java 8
* git


* [Hosted on heroku](https://bank-mapdb-api.herokuapp.com/)


##Examples of use
Test endpoints from heroku on PostMapping or replace "bank-mapdb-api.herokuapp.com" with localhost for testing locally.
###Available endpoints
####Register
https://bank-mapdb-api.herokuapp.com/banking-app/register

{

"accountName": "Ikpea Ehigie",

"initialDeposit": 600.00,

"accountPassword":"through"

}
####Login
https://bank-mapdb-api.herokuapp.com/banking-app/login

{

"accountNumber":"0305347459",

"password":"through"

}

 login will return a token which you'll have to use for performing other authenticated activities(deposit, withdraw, statement of accout account info and account balance)

Project status
Sources
Other information
