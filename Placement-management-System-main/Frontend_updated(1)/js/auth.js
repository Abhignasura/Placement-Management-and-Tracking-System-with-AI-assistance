const API_BASE = "http://localhost:8080/api";

async function loginAdmin(){

const email = document.getElementById("email").value.trim();
const password = document.getElementById("password").value.trim();

/* stop empty login */

if(email === "" || password === ""){
alert("Please enter email and password");
return;
}

try{

const response = await fetch(API_BASE + "/auth/login",{

method:"POST",

headers:{
"Content-Type":"application/json"
},

body:JSON.stringify({
email:email,
password:password
})

});

const data = await response.json();

console.log("Login response:", data);

/* if login failed */

if(!data.token){
alert("Invalid credentials");
return;
}

/* store token + role */

localStorage.setItem("token", data.token);
localStorage.setItem("role", data.role);

/* redirect based on role */

if(data.role === "ADMIN"){
window.location.href = "admin/dashboard.html";
}

else if(data.role === "STUDENT"){
window.location.href = "student/dashboard.html";
}

else if(data.role === "RECRUITER"){
window.location.href = "recruiter/dashboard.html";
}

}catch(error){

console.error("Login error:", error);
alert("Login failed");

}

}