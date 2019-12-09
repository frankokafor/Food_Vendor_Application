# Food_Vendor_Application

Application Begins

Get full application api documentation at localhost:8081/food_vendor/swagger-ui.html
1)Application Developed with springboot
2)Implimentation of spring security
3)Rest end-point with authenticated urls

APPLICATION WALK THROUGH
1)All created users are assigned with user role
2)send required user details to appropriate endpoint to make user an admin for admin priviledges
3)when user is created, an email verification token is sent to the user's email. send token to appropriate endpoint to verify user for login access
4)There is a websocket endpoint for live chat between user and admin
5)When an order is made, all users with admin role will get a notification
6)when a new food or drink is added,all users with user role will get a notification
7)when user forgets password, a password verification token is sent to his/her registered email. send token to appropriate endpoint to update user password
8)images can be uploaded for users, food and drinks.
9)pay pal api implemented for online and card payments.
10)
