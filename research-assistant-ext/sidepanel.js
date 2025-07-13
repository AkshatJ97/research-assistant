document.addEventListener('DOMContentLoaded', () => {
    chrome.storage.local.get(['researchNotes'], function(result) {
        if (result.researchNotes) {
            document.getElementById('notes').value = result.researchNotes;
        }
    });

    document.getElementById('summarizeBtn').addEventListener('click', () => handleTextOperation('summarize'));
    document.getElementById('paraphraseBtn').addEventListener('click', () => handleTextOperation('paraphrase'));
    document.getElementById('suggestBtn').addEventListener('click', () => handleTextOperation('suggest'));
    document.getElementById('citeBtn').addEventListener('click', () => handleTextOperation('cite'));
    document.getElementById('defineBtn').addEventListener('click', () => handleTextOperation('define'));
    document.getElementById('saveNotesBtn').addEventListener('click', saveNotes);
    document.getElementById('exportNotesBtn').addEventListener('click', exportNotes);
});

async function handleTextOperation(operation) {
    try {
        const [tab] = await chrome.tabs.query({active: true, currentWindow: true});
        const [{result}] = await chrome.scripting.executeScript({
            target: {tabId: tab.id},
            function: () => window.getSelection().toString()
        });
        if (!result) {
            showResult('Please select some text first');
            return;
        }
        let body = {content: result, operation: operation.toUpperCase()};
        if (operation === 'cite') {
            // For citation, send "Title|URL"
            body.content = `${tab.title}|${tab.url}`;
        }
        const response = await fetch('http://localhost:8080/api/research/process', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });
        if (!response.ok) {
            throw new Error(`API Error: ${response.status}`);
        }
        const text = await response.text();
        showResult(text.replace(/\n/g, '<br>'));
    } catch (error) {
        showResult('Error: ' + error.message);
    }
}

async function saveNotes() {
    const notes = document.getElementById('notes').value;
    chrome.storage.local.set({'researchNotes': notes}, function() {
        alert('Notes saved successfully');
    });
}

function exportNotes() {
    const notes = document.getElementById('notes').value;
    if (!notes) {
        alert('No notes to export!');
        return;
    }

    const format = document.getElementById('exportFormat').value;
    const mime = format === 'doc' ? "application/msword" : "text/plain";
    const blob = new Blob([notes], { type: mime });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = format === 'doc' ? "research-notes.doc" : "research-notes.txt";
    a.click();
    URL.revokeObjectURL(url);
}

function showResult(content) {
    document.getElementById('results').innerHTML = `<div class="result-item"><div class="result-content">${content}</div></div>`;
}
