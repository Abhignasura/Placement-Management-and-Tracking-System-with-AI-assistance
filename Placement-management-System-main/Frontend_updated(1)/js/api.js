const API_BASE = "http://localhost:8080/api";

function getToken(){
    return localStorage.getItem("token");
}

function apiGet(url){

    return fetch(API_BASE + url,{

        headers:{
            "Authorization":"Bearer " + localStorage.getItem("token")
        }

    });

}

function apiPost(url, body){

    return fetch(API_BASE + url,{

        method:"POST",

        headers:{
            "Content-Type":"application/json",
            "Authorization":"Bearer " + getToken()
        },

        body: JSON.stringify(body)

    });

}