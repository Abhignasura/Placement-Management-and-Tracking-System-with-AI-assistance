```javascript
document.addEventListener("DOMContentLoaded", function () {

    const chatbotHTML = `
    
    <div class="chat-icon" onclick="toggleChat()">💬</div>

    <div class="chat-container" id="chatContainer">
        <div class="chat-header">
            Placement Assistant
            <span onclick="toggleChat()">✖</span>
        </div>

        <div class="chat-body" id="chatBody">
            <div class="bot-message">Hi 👋 Ask me about placements, CGPA, companies or eligibility.</div>
        </div>

        <div class="chat-footer">
            <input type="text" id="userInput" placeholder="Type your question..." onkeypress="handleKey(event)">
            <button onclick="sendMessage()">Send</button>
        </div>
    </div>

    `;

    document.body.insertAdjacentHTML("beforeend", chatbotHTML);

});
```
