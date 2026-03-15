
const faqDatabase = [

{
question: "How do I login to the system?",
answer: "You can login using your registered email and password on the login page.",
keywords: ["login","signin"]
},

{
question: "How do I register as a student?",
answer: "Students can register using their official college email ID.",
keywords: ["register","signup"]
},

{
question: "What is the placement process?",
answer: "The placement process generally includes Aptitude, Technical and HR rounds.",
keywords: ["placement","process","round"]
},

{
question: "How is eligibility calculated?",
answer: "Eligibility is calculated based on branch and CGPA requirements set by companies.",
keywords: ["eligibility","cgpa"]
},

{
question: "Where can I see placement statistics?",
answer: "Placement statistics are available in the admin dashboard under the analytics section.",
keywords: ["statistics","analytics","stats"]
},

{
question: "How many rounds are there in placements?",
answer: "Usually there are three rounds: Aptitude, Technical and HR.",
keywords: ["round"]
},

{
question: "What does the admin do?",
answer: "Admin manages users, monitors placements, and views analytics reports.",
keywords: ["admin"]
},

{
question: "What does a recruiter do?",
answer: "Recruiters can post jobs, shortlist students and schedule interviews.",
keywords: ["recruiter","company"]
},

{
question: "What can students do in this portal?",
answer: "Students can view companies, apply for jobs and track their placement status.",
keywords: ["student"]
}

];

function sendMessage(){

const input = document.getElementById("userInput");
const message = input.value.trim().toLowerCase();

if(!message) return;

const chatBody = document.getElementById("chatBody");

chatBody.innerHTML += '<div class="user-message">'+message+'</div>';

input.value="";

const matches = faqDatabase.filter(faq =>
faq.keywords.some(keyword => message.includes(keyword))
);

if(matches.length > 0){

chatBody.innerHTML += '<div class="bot-message">Did you mean:</div>';

matches.forEach((faq,index)=>{

chatBody.innerHTML +=
'<div class="bot-message suggestion" onclick="showAnswer('+faqDatabase.indexOf(faq)+')">'
+faq.question+
'</div>';

});

}
else{

chatBody.innerHTML +=
'<div class="bot-message">Sorry, I could not find a related question.</div>';

}

chatBody.scrollTop = chatBody.scrollHeight;

}

function showAnswer(index){

const chatBody = document.getElementById("chatBody");

chatBody.innerHTML +=
'<div class="bot-message">'+faqDatabase[index].answer+'</div>';

chatBody.scrollTop = chatBody.scrollHeight;

}

function handleKey(event){

if(event.key === "Enter"){
sendMessage();
}

}

function toggleChat(){

const chat = document.getElementById("chatContainer");

chat.classList.toggle("active");

}

