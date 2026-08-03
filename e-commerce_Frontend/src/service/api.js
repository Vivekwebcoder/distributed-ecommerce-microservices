import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json",
    },
});

// Add JWT token automatically
api.interceptors.request.use(
    (config) => {

        const token = localStorage.getItem("token");

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// =========================================================
// FORGOT PASSWORD
// =========================================================

export const forgotPassword = async (data) => {

    const response = await api.post(
        "/api/auth/forgot-password",
        data
    );

    return response.data;
};


export default api;