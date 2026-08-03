import api from "./api";

// Admin API
export const getAdminData = async () => {

    const response = await api.get(
        "/api/role/admin"
    );

    return response.data;
};

// Seller API
export const getSellerData = async () => {

    const response = await api.get(
        "/api/role/seller"
    );

    return response.data;
};

// Customer API
export const getCustomerData = async () => {

    const response = await api.get(
        "/api/role/customer"
    );

    return response.data;
};

// Admin + Seller API
export const getAdminSellerData = async () => {

    const response = await api.get(
        "/api/role/admin-seller"
    );

    return response.data;
};