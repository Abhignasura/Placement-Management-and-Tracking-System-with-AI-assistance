```javascript
const faqDatabase = [
  {
    question: "How do I login to the system?",
    answer: "You can login using your registered email and password from the login page.",
    keywords: ["login", "log in", "sign in"]
  },
  {
    question: "How do I register?",
    answer: "Students can register using their official college email ID.",
    keywords: ["register", "registration", "signup"]
  },
  {
    question: "What is the placement process?",
    answer: "Placement process generally includes Aptitude, Technical and HR rounds.",
    keywords: ["placement", "process", "rounds"]
  },
  {
    question: "What does the admin do?",
    answer: "Admin monitors placement statistics and manages system data.",
    keywords: ["admin"]
  },
  {
    question: "What does a company user do?",
    answer: "Company users can shortlist students and manage recruitment rounds.",
    keywords: ["company"]
  },
  {
    question: "What does a student user do?",
    answer: "Students can view eligible companies and track their placement status.",
    keywords: ["student"]
  },
  {
    question: "How is eligibility calculated?",
    answer: "Eligibility is calculated based on branch and CGPA requirements of companies.",
    keywords: ["eligibility", "eligible", "cgpa"]
  },
  {
    question: "How can I reset my password?",
    answer: "Please contact the admin to reset your password.",
    keywords: ["password", "forgot"]
  },
  {
    question: "Where can I see placement statistics?",
    answer: "Placement statistics are available in the admin dashboard under analytics section.",
    keywords: ["statistics", "stats", "analytics"]
  },
  {
    question: "How many rounds are there in placements?",
    answer: "Usually there are 3 rounds: Aptitude, Technical and HR.",
    keywords: ["round"]
  }
];

function sendMessage() {

  const input = document.getElementById("userInput");
  const message = input.value.trim().toLowerCase();

  if (!message) return;

  const chatBody = document.getElementById("chatBody");

  chatBody.innerHTML += `<div class="user-message">${message}</div>`;

  input.value = "";

  chatBody.scrollTop = chatBody.scrollHeight;

  // typing animation
  const typingDiv = document.createElement("div");
  typingDiv.className = "bot-message";
  typingDiv.innerText = "Typing...";
  chatBody.appendChild(typingDiv);

  setTimeout(() => {

    typingDiv.remove();

    const matches = faqDatabase.filter(faq =>
      faq.keywords.some(keyword => message.includes(keyword))
    );

    if (matches.length > 0) {

      let suggestionHTML = `<div class="bot-message">Here are some things I found:</div>`;

      matches.forEach((faq, index) => {

        suggestionHTML += `
        <div class="bot-message suggestion" onclick="showAnswer(${faqDatabase.indexOf(faq)})">
        ${faq.question}
        </div>
        `;

      });

      chatBody.innerHTML += suggestionHTML;

    } else {

      chatBody.innerHTML += `
      <div class="bot-message">
      Sorry 😔 I couldn't find an answer for that.<br>
      Try asking about login, placement rounds, CGPA or statistics.
      </div>
      `;

    }

    chatBody.scrollTop = chatBody.scrollHeight;

  }, 800);

}

function showAnswer(index) {

  const chatBody = document.getElementById("chatBody");

  chatBody.innerHTML += `
  <div class="bot-message">${faqDatabase[index].answer}</div>
  `;

  chatBody.scrollTop = chatBody.scrollHeight;

}

function handleKey(event) {

  if (event.key === "Enter") {
    sendMessage();
  }

}

function toggleChat() {

  const chat = document.getElementById("chatContainer");

  chat.classList.toggle("active");

}
```
