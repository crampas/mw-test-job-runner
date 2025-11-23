
const fetchRun = async (config) => {
    const response = await fetch(`/api/job/${config.jobName}/run/${config.runId}`);
    if (!response.ok) {
        throw new Error(`Response status: ${response.status}`);
    }
    const result = await response.json();
    return result.data;
}

const startLogUpdater = async (config) => {
    const jobName = config.jobName;
    const runId = config.runId;
    const errorElementId = config.errorElementId;
    const logElementId = config.logElementId;
    let last = 0;
    const updateLogDiv = async () => {
        const response = await fetch(`http://localhost:8080/api/job/${jobName}/run/${runId}/logStream?last=${last}`);
        if (!response.ok) {
          throw new Error(`Response status: ${response.status}`);
        }
        const result = await response.json();
        const errorDiv = document.getElementById(errorElementId);
        if (result.error) {
            errorDiv.innerHTML = result.error;
            return;
        }
        errorDiv.innerHTML = '';

        const lines = result.data.lines;
        last = result.data.next;

        const logDiv = document.getElementById(logElementId);
        for (const line of lines) {
            const logLineElement = document.createElement("div");
            logLineElement.classList.add("log-line");
            logLineElement.append(line);
            logDiv.append(logLineElement);
        }
    }

    updateLogDiv();
    setInterval(updateLogDiv, 1000);
}

