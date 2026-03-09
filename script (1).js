// LOGIN
function login(){
    let user = document.getElementById("username").value;
    let pass = document.getElementById("password").value;

    if(user === "" || pass === ""){
        alert("Enter credentials");
        return;
    }

    document.getElementById("loginPage").style.display = "none";
    document.getElementById("dashboardPage").style.display = "block";
}

// GOOGLE LOGIN (Improved)
function googleLogin(){

    let email = prompt("Enter your Google Gmail address:");

    if(email === null || email.trim() === ""){
        alert("Google login cancelled");
        return;
    }

    if(!email.endsWith("@gmail.com")){
        alert("Please enter valid Gmail address");
        return;
    }

    alert("Login Successful: " + email);

    document.getElementById("loginPage").style.display = "none";
    document.getElementById("dashboardPage").style.display = "block";
}

// SIGNUP
function signup(){
    alert("Signup Successful (Demo)");
}

// LOGOUT
function logout(){
    document.getElementById("dashboardPage").style.display = "none";
    document.getElementById("loginPage").style.display = "block";
}

// ANALYZE RESUME
async function analyzeResume(){

    const file = document.getElementById("resumeFile").files[0];

    if(!file){
        alert("Upload resume first");
        return;
    }

    let text = "";

    if(file.type === "text/plain"){
        text = await file.text();
    }
    else if(file.type === "application/pdf"){
        text = await extractPDFText(file);
    }
    else{
        alert("Unsupported file format");
        return;
    }

    generateScore(text);
}

// EXTRACT PDF TEXT
async function extractPDFText(file){
    const reader = new FileReader();

    return new Promise((resolve)=>{
        reader.onload = async function(){
            const typedarray = new Uint8Array(this.result);
            const pdf = await pdfjsLib.getDocument(typedarray).promise;

            let fullText = "";

            for(let i=1; i<=pdf.numPages; i++){
                const page = await pdf.getPage(i);
                const content = await page.getTextContent();
                const strings = content.items.map(item => item.str);
                fullText += strings.join(" ");
            }

            resolve(fullText);
        };
        reader.readAsArrayBuffer(file);
    });
}

// GENERATE SCORE + STAR RATING
function generateScore(text){

    let score = 0;
    let suggestions = [];

    text = text.toLowerCase();

    const skills = ["java","python","javascript","react","node","c++"];
    let skillCount = skills.filter(skill => text.includes(skill)).length;

    score += skillCount * 10;

    if(skillCount < 3)
        suggestions.push("Add more technical skills to strengthen your resume.");

    if(text.includes("project"))
        score += 15;
    else
        suggestions.push("Include detailed project section with technologies used.");

    if(text.includes("experience"))
        score += 20;
    else
        suggestions.push("Mention internships or work experience.");

    if(text.includes("certificate"))
        score += 10;
    else
        suggestions.push("Add certifications to improve credibility.");

    if(text.includes("cgpa"))
        score += 10;
    else
        suggestions.push("Mention your CGPA or academic performance.");

    if(score > 100) score = 100;

    let stars = getStarRating(score);

    document.getElementById("scoreCard").innerHTML =
        "<h3>Resume Score</h3>" +
        "<h1>" + score + " / 100</h1>" +
        "<div class='stars'>" + stars + "</div>";

    let suggestionHTML = "<h3>Improvement Suggestions</h3>";

    if(suggestions.length === 0){
        suggestionHTML += "<div class='good'>Excellent Resume! No major improvements needed.</div>";
    }
    else{
        suggestions.forEach(item => {
            suggestionHTML += "<div class='suggestion-item'>✔ " + item + "</div>";
        });
    }

    document.getElementById("suggestions").innerHTML = suggestionHTML;
}

// STAR LOGIC
function getStarRating(score){

    let starCount = 0;

    if(score <= 20) starCount = 1;
    else if(score <= 40) starCount = 2;
    else if(score <= 60) starCount = 3;
    else if(score <= 80) starCount = 4;
    else starCount = 5;

    let stars = "";

    for(let i=0;i<5;i++){
        if(i < starCount)
            stars += "⭐";
        else
            stars += "☆";
    }

    return stars;
}