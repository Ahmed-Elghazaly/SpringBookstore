import axios from 'axios';

// Change this URL if testing locally without Docker (e.g., http://localhost:8080)
const API_BASE_URL = "http://localhost:8080";

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const bookApi = {
    getAll: () => api.get('/books'),
    search: (params) => api.get('/books/search', { params }), // params: title, category, author
    getOne: (isbn) => api.get(`/books/${isbn}`),
    create: (data) => api.post('/books', data),
};

export const cartApi = {
    get: (customerId) => api.get(`/api/cart/${customerId}`),
    add: (customerId, item) => api.post(`/api/cart/${customerId}/items`, item), // item: { isbn, quantity }
    update: (customerId, isbn, qty) => api.put(`/api/cart/${customerId}/items/${isbn}?quantity=${qty}`),
    remove: (customerId, isbn) => api.delete(`/api/cart/${customerId}/items/${isbn}`),
    checkout: (customerId) => api.post(`/api/orders/checkout/${customerId}`),
};

export const orderApi = {
    getHistory: (customerId) => api.get(`/api/orders/customer/${customerId}`),
};

export const reportApi = {
    getTopCustomers: () => api.get('/api/orders/reports/top-customers'),
    getTopBooks: () => api.get('/api/orders/reports/top-books'),
    getSalesByDate: (date) => api.get(`/api/orders/reports/sales/date?date=${date}`),
    getSalesPrevMonth: () => api.get('/api/orders/reports/sales/prev-month'),
};

export const customerApi = {
    register: (data) => api.post('/customers', data)
}

export default api;