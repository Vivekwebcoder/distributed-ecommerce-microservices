// Check whether user is logged in
export const isAuthenticated = () => {

    const token = localStorage.getItem("token");

    return token !== null;
};


// Get logged-in user's role
export const getUserRole = () => {

    return localStorage.getItem("role");
};


// Get logged-in user's email
export const getUserEmail = () => {

    return localStorage.getItem("email");
};