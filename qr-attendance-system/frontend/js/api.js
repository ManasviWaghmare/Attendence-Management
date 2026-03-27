const BASE_URL = 'http://localhost:8080/api';

const fetchStats = async () => {
    try {
        const response = await fetch(`${BASE_URL}/dashboard/stats`);
        const data = await response.json();
        return {
            totalStudents: data.totalStudents || 0,
            totalPresent: data.totalAttendances || 0, // Maps backend totalAttendances to frontend totalPresent
            activeSessions: data.activeSessions || 0
        };
    } catch (error) {
        console.error('Error fetching stats:', error);
        return { totalStudents: 0, totalPresent: 0, activeSessions: 0 };
    }
};

const generateQR = async (courseId) => {
    try {
        const response = await fetch(`${BASE_URL}/qr/generate/${courseId}`, {
            method: 'POST'
        });
        const data = await response.json();
        return data; // Returns { qrImage, sessionId, token }
    } catch (error) {
        console.error('Error generating QR:', error);
        return null;
    }
};

const getCurrentQR = async (sessionId) => {
    try {
        const response = await fetch(`${BASE_URL}/qr/current/${sessionId}`);
        const data = await response.json();
        return data; // Returns { qrImage, sessionId }
    } catch (error) {
        console.error('Error fetching current QR:', error);
        return null;
    }
};

const getActiveSession = async (courseId) => {
    try {
        const response = await fetch(`${BASE_URL}/qr/active/${courseId}`);
        const data = await response.json();
        return data; // Returns { active: boolean, sessionId: long }
    } catch (error) {
        console.error('Error fetching active session:', error);
        return { active: false };
    }
};

const fetchSessionAnalytics = async (sessionId) => {
    try {
        const response = await fetch(`${BASE_URL}/dashboard/session/${sessionId}`);
        return await response.json();
    } catch (error) {
        console.error('Error fetching analytics:', error);
        return null;
    }
};

const markAttendance = async (attendanceData) => {
    try {
        const response = await fetch(`${BASE_URL}/attendance/mark`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(attendanceData)
        });
        return await response.json();
    } catch (error) {
        console.error('Error marking attendance:', error);
        return { error: 'Failed to connect to server' };
    }
};
