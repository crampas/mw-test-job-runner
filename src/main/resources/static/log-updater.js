
let last = 0;
const updateLogDiv = async () => {
    const response = await fetch(`http://localhost:8080/api/job/org.example.jmeter.JMeterBatchJob/run/org.example.jmeter.JMeterBatchJob-1/logStream?last=${last}`);
    if (!response.ok) {
      throw new Error(`Response status: ${response.status}`);
    }
    const result = await response.json();
    const lines = result.data.lines;
    last = result.data.next;

    const logDiv = document.getElementById("log");
    for (const line of lines) {
        const logLineElement = document.createElement("div");
        logLineElement.classList.add("log-line");
        logLineElement.append(line);
        logDiv.append(logLineElement);
    }
}

updateLogDiv();
setInterval(updateLogDiv, 1000);
