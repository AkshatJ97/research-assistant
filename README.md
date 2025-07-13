# Research Assistant

**Research Assistant** is an AI-powered Chrome extension and backend service that streamlines research tasks directly within your browser. It provides advanced text processing features—such as summarization, paraphrasing, topic suggestion, citation generation, and definition lookup—through an interactive side panel, backed by a Spring Boot server that integrates with Google Gemini for state-of-the-art AI language capabilities.

---

## Features

- **Summarize**: Condenses selected text into a clear, concise summary.
- **Paraphrase**: Rewrites selected content in simpler, alternative language.
- **Suggest**: Offers related topics and further reading suggestions.
- **Generate Citation**: Instantly generates APA-style citations for the current page.
- **Define**: Provides definitions for selected words/phrases.
- **Research Notes**: Lets users take, save, and export research notes in TXT or DOC formats.
- **Local Storage**: Persists notes between sessions using browser storage.

---

## How It Works

The system consists of two main parts:

- **Chrome Extension (`research-assistant-ext`)**
  - Presents a side panel with interactive controls.
  - On user action, captures selected text or page metadata.
  - Sends a request to the backend service for processing.
  - Displays AI-generated results directly in the side panel.
  - Allows users to take and export personal research notes.

- **Backend Service (`research-assistant-sb`)**
  - REST API built with Spring Boot.
  - Receives processing requests from the extension.
  - For citation generation, creates simple APA-style citations using page title and URL.
  - For other operations (summarize, paraphrase, suggest, define), crafts a prompt and delegates to the Gemini LLM API.
  - Parses LLM responses and returns the result to the extension.
    
---

## Demo Video 
[🎥 Watch Demo](https://www.loom.com/share/e6d08ea6be634fcda083c46d25cba048?sid=0835a7d8-4d48-4c1e-92ec-c084836fcc28)

---

## Project Structure

```
research-assistant/
├── research-assistant-ext/
│   ├── manifest.json
│   ├── background.js
│   ├── sidepanel.html
│   ├── sidepanel.js
│   └── sidepanel.css
├── research-assistant-sb/
│   ├── pom.xml
│   ├── src/
│   │   ├── main/java/com/research/assistant/
│   │   │   ├── ResearchAssistantApplication.java
│   │   │   ├── ResearchController.java
│   │   │   ├── ResearchService.java
│   │   │   ├── ResearchRequest.java
│   │   │   ├── ResearchOperation.java
│   │   │   └── GeminiResponse.java
│   │   └── main/resources/
│   │       └── application.properties
│   └── .mvn/
│       └── wrapper/maven-wrapper.properties
```

---

## Tech Stack

**Frontend:**
- **Chrome Extension** (Manifest V3)
  - HTML, CSS, JavaScript
  - Uses Chrome APIs: `sidePanel`, `storage`, `scripting`, etc.

**Backend:**
- **Spring Boot** (Java 21)
- **WebFlux** for reactive HTTP API calls
- **Google Gemini LLM API** (requires API Key/URL via environment variables)
- **Jackson** for JSON handling
- **Lombok** for boilerplate Java code

---

## Usage Instructions

### Prerequisites

- **Java 21+** and **Maven** for backend
- **Chrome** browser (latest version) for extension
- **Google Gemini API Key** and endpoint URL

### Setup

#### 1. Start the Backend

```bash
cd research-assistant-sb
# Set environment variables for Gemini API (or edit application.properties)
export GEMINI_URL='https://YOUR_GEMINI_ENDPOINT/'
export GEMINI_KEY='YOUR_API_KEY'
./mvnw spring-boot:run
```

#### 2. Load the Extension

1. Open Chrome and navigate to `chrome://extensions`.
2. Enable "Developer Mode".
3. Click "Load unpacked" and select the `research-assistant-ext` folder.
4. The "Research Assistant" icon will appear in your browser's extensions panel.

#### 3. Using the Assistant

- Select text on any webpage.
- Click the Research Assistant extension icon to open the side panel.
- Click any of the available action buttons (Summarize, Paraphrase, Suggest, Cite, Define).
- Results will appear in the panel.
- Use the notes section to save or export your own notes.

---

## Details

- **End-to-End Solution:** Demonstrates full-stack development skills—custom Chrome extension, RESTful backend, and integration with LLM APIs.
- **Modern Technologies:** Utilizes Manifest V3, Chrome side panel API, Java 21, Spring Boot 3.x, and reactive programming with WebFlux.
- **AI Integration:** Abstracts prompt engineering and Gemini model invocation into a reusable backend service.
- **Robust UX:** Includes persistence (local storage), file export, and real-time API results for an efficient research workflow.
- **Scalable Design:** API-driven architecture makes it simple to swap out the language model or expand functionality.

---

## Customization & Extensibility

- **Adding Operations:** Extend `ResearchOperation.java` and update prompt crafting logic in `ResearchService.java`.
- **Alternate LLMs:** Swap Gemini endpoint with any LLM that accepts similar prompts and responses.
- **UI Enhancements:** The side panel HTML/CSS and JS are modular for easy branding and feature additions.

---

*For further details or demo requests, please see code comments and controller/service logic in the backend, or interact with the live extension UI.*
