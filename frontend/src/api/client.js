import axios from 'axios';

// Base URL for all API calls
const API_BASE_URL = "/api";

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {'Content-Type': 'application/json'},
});

// ==================== BOOK API ====================
export const bookApi = {
    getAll: () => api.get('/books'),
    search: (params) => api.get('/books/search', {params}),
    getOne: (isbn) => api.get(`/books/${isbn}`),
    create: (data) => api.post('/books', data),
    update: (isbn, data) => api.put(`/books/${isbn}`, data),
};

// ==================== CART API ====================
export const cartApi = {
    get: (customerId) => api.get(`/cart/${customerId}`),
    add: (customerId, item) => api.post(`/cart/${customerId}/items`, item),
    update: (customerId, isbn, qty) => api.put(`/cart/${customerId}/items/${isbn}?quantity=${qty}`),
    remove: (customerId, isbn) => api.delete(`/cart/${customerId}/items/${isbn}`),
    // Fixed: Logic for clearing cart on logout was using wrong path in Navbar,
    // but the API definition here is fine.
    clear: (customerId) => api.delete(`/cart/${customerId}`),
    checkout: (customerId, cardData) => api.post(`/orders/checkout/${customerId}`, cardData),
};

// ==================== ORDER API ====================
export const orderApi = {
    getHistory: (customerId) => api.get(`/orders/history/${customerId}`), // Fixed path from /orders/customer/
};

// ==================== REPORT API ====================
export const reportApi = {
    getTopCustomers: () => api.get('/orders/reports/top-customers'),
    getTopBooks: () => api.get('/orders/reports/top-books'),

    // Fixed: Backend expects /sales-by-date, not /sales/date
    getSalesByDate: (date) => api.get(`/orders/reports/sales-by-date?date=${date}`),

    // Fixed: Backend expects /sales-previous-month, not /sales/prev-month
    getSalesPrevMonth: () => api.get('/orders/reports/sales-previous-month'),

    // NOTE: You are missing the endpoint for this in your OrderController.java.
    // It exists in Service but is not exposed in Controller. This will still 404 until you fix backend.
    getBookOrderCount: (isbn) => api.get(`/orders/reports/publisher-orders/${isbn}`),
};

// ==================== CUSTOMER API ====================
export const customerApi = {
    // Fixed: Backend endpoint is /register, not root
    register: (data) => api.post('/customers/register', data)
};

// ==================== AUTH API ====================
export const authApi = {
    login: (data) => api.post('/auth/login', data)
};

// ==================== PUBLISHER ORDER API ====================
export const publisherOrderApi = {
    getPending: () => api.get('/publisher-orders/pending'),
    confirm: (orderId) => api.post(`/publisher-orders/${orderId}/confirm`),
};

// ==================== PUBLISHER & CATEGORY API ====================
export const publisherApi = {
    getAll: () => api.get('/publishers'),
};

export const categoryApi = {
    getAll: () => api.get('/categories'),
};

export default api;