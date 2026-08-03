import api from "./api";

// Register User
export const registerUser = async (registerData) => {
    const response = await api.post(
        "/api/auth/register",
        registerData
    );

    return response.data;
};

// Login User
export const loginUser = async (loginData) => {
    const response = await api.post(
        "/api/auth/login",
        loginData
    );

    return response.data;
};
// Logout User
export const logoutUser = () => {

    localStorage.removeItem("token");
    localStorage.removeItem("role");
    localStorage.removeItem("email");

    window.location.href = "/login";
};
export const resetPassword = async (data) => {

    const response = await api.post(
        "/auth/reset-password",
        data
    );

    return response.data;
};