// Global State
let currentSessionId = null;
let qrPollingInterval = null;

// DOM Elements
const statTotalStudents = document.getElementById('stat-total-students');
const statTotalPresent = document.getElementById('stat-total-present');
const statActiveSessions = document.getElementById('stat-active-sessions');
const attendanceChartCanvas = document.getElementById('attendanceChart');
const courseSelector = document.getElementById('course-selector');
const btnStartSession = document.getElementById('btn-start-session');
const btnStopSession = document.getElementById('btn-stop-session');
const qrDisplay = document.getElementById('qr-display');
const qrImage = document.getElementById('qr-image');
const qrPlaceholder = document.getElementById('qr-placeholder');

// Initialize Dashboard
const initDashboard = async () => {
    // 1. Initial Data Fetch
    const stats = await fetchStats();
    updateUIStats(stats);

    // 2. Initialize Chart
    if (attendanceChartCanvas) {
        initAttendanceChart(attendanceChartCanvas.getContext('2d'));
    }

    // 3. Event Listeners
    btnStartSession.addEventListener('click', handleStartSession);
    btnStopSession.addEventListener('click', handleStopSession);

    // 4. Check for existing active sessions if a course is pre-selected
    checkAndResumeSession();
};

const checkAndResumeSession = async () => {
    const courseId = courseSelector.value;
    if (!courseId) return;

    const sessionInfo = await getActiveSession(courseId);
    if (sessionInfo.active) {
        currentSessionId = sessionInfo.sessionId;
        resumeSessionUI();
    }
};

const resumeSessionUI = async () => {
    const data = await getCurrentQR(currentSessionId);
    if (data && data.qrImage) {
        qrImage.src = `data:image/png;base64,${data.qrImage}`;
        qrDisplay.style.display = 'block';
        qrPlaceholder.style.display = 'none';
        btnStartSession.style.display = 'none';
        btnStopSession.style.display = 'block';
        courseSelector.disabled = true;
        
        startQRPolling(currentSessionId);
        startLogPolling(currentSessionId);
    }
};

// Start Session Handler
const handleStartSession = async () => {
    const courseId = courseSelector.value;
    if (!courseId) {
        alert('Please select a course first!');
        return;
    }

    try {
        const data = await generateQR(courseId);
        if (data && data.qrImage) {
            currentSessionId = data.sessionId;
            // Update UI State
            qrImage.src = `data:image/png;base64,${data.qrImage}`;
            qrDisplay.style.display = 'block';
            qrPlaceholder.style.display = 'none';
            btnStartSession.style.display = 'none';
            btnStopSession.style.display = 'block';
            courseSelector.disabled = true;

            // Start polling for rotations
            startQRPolling(currentSessionId);
            startLogPolling(currentSessionId);
        }
    } catch (error) {
        console.error('Session start failed:', error);
    }
};

// Stop Session Handler
const handleStopSession = () => {
    stopQRPolling();
    stopLogPolling();
    qrDisplay.style.display = 'none';
    qrPlaceholder.style.display = 'flex';
    btnStartSession.style.display = 'block';
    btnStopSession.style.display = 'none';
    courseSelector.disabled = false;
    qrImage.src = "";
    currentSessionId = null;
};

// State Update Utilities
const updateUIStats = (stats) => {
    statTotalStudents.innerText = stats.totalStudents || 0;
    statTotalPresent.innerText = stats.totalPresent || 0;
    statActiveSessions.innerText = stats.activeSessions || 0;
};

// QR Polling Logic
const startQRPolling = (sessionId) => {
    currentSessionId = sessionId;
    if (qrPollingInterval) clearInterval(qrPollingInterval);
    qrPollingInterval = setInterval(async () => {
        if (!currentSessionId) return;
        const data = await getCurrentQR(currentSessionId);
        if (data && data.qrImage) {
            qrImage.src = `data:image/png;base64,${data.qrImage}`;
        }
    }, 10000); // Poll every 10 seconds to match server rotation
};

const stopQRPolling = () => {
    if (qrPollingInterval) {
        clearInterval(qrPollingInterval);
        qrPollingInterval = null;
    }
};

let logPollingInterval = null;
const startLogPolling = (sessionId) => {
    if (logPollingInterval) clearInterval(logPollingInterval);
    updateAttendanceLog(sessionId); // Initial call
    logPollingInterval = setInterval(() => updateAttendanceLog(sessionId), 5000);
};

const stopLogPolling = () => {
    if (logPollingInterval) {
        clearInterval(logPollingInterval);
        logPollingInterval = null;
    }
};

const updateAttendanceLog = async (sessionId) => {
    const analytics = await fetchSessionAnalytics(sessionId);
    if (!analytics || !analytics.logs) return;

    const logBody = document.getElementById('attendance-log');
    if (!logBody) return;

    if (analytics.logs.length === 0) {
        logBody.innerHTML = `<tr><td colspan="5" style="text-align: center; color: var(--text-muted);">No records yet for this session.</td></tr>`;
        return;
    }

    logBody.innerHTML = analytics.logs.map(log => `
        <tr>
            <td>${log.name}</td>
            <td>${log.rollNumber}</td>
            <td><code style="background: rgba(255,255,255,0.05); padding: 2px 6px; border-radius: 4px;">Verified Device</code></td>
            <td>${log.time}</td>
            <td><span class="status-badge status-${log.status.toLowerCase()}">${log.status}</span></td>
        </tr>
    `).join('');

    // Update stats too
    statTotalPresent.innerText = analytics.presentCount;
};

// Run on page load
window.addEventListener('load', initDashboard);
