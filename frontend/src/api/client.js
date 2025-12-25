import axios from 'axios';

// Base URL for all API calls - this is proxied by nginx to the backend
const API_BASE_URL = "/api";

// Create an axios instance with default configuration
// This ensures all requests have the correct base URL and headers
const api = axios.create({
    baseURL: API_BASE_URL, 
    headers: {'Content-Type': 'application/json'},
});

// ==================== BOOK API ====================
// Endpoints for managing books (browsing, searching, CRUD operations)
export const bookApi = {
    // Get all books in the system
    getAll: () => api.get('/books'),
    
    // Search books with optional filters (isbn, title, category, author, publisher)
    search: (params) => api.get('/books/search', {params}),
    
    // Get a single book by its ISBN
    getOne: (isbn) => api.get(`/books/${isbn}`),
    
    // Create a new book (admin only)
    create: (data) => api.post('/books', data),
    
    // Update an existing book (admin only)
    // Only title, price, stock, and threshold can be modified
    update: (isbn, data) => api.put(`/books/${isbn}`, data),
};

// ==================== CART API ====================
// Endpoints for shopping cart operations
export const cartApi = {
    // Get the current cart contents for a customer
    get: (customerId) => api.get(`/cart/${customerId}`),
    
    // Add a book to the cart (or increase quantity if already in cart)
    add: (customerId, item) => api.post(`/cart/${customerId}/items`, item),
    
    // Update the quantity of a book in the cart
    update: (customerId, isbn, qty) => api.put(`/cart/${customerId}/items/${isbn}?quantity=${qty}`),
    
    // Remove a book from the cart
    remove: (customerId, isbn) => api.delete(`/cart/${customerId}/items/${isbn}`),
    
    // Complete the checkout process with credit card information
    checkout: (customerId, cardData) => api.post(`/orders/checkout/${customerId}`, cardData),
};

// ==================== ORDER API ====================
// Endpoints for order history
export const orderApi = {
    // Get all past orders for a customer with book details
    getHistory: (customerId) => api.get(`/orders/customer/${customerId}`),
};

// ==================== REPORT API ====================
// Endpoints for admin reports
export const reportApi = {
    // Get top 5 customers by total spending (last 3 months)
    getTopCustomers: () => api.get('/orders/reports/top-customers'),
    
    // Get top 10 selling books by quantity sold (last 3 months)
    getTopBooks: () => api.get('/orders/reports/top-books'),
    
    // Get total sales for a specific date
    getSalesByDate: (date) => api.get(`/orders/reports/sales/date?date=${date}`),
    
    // Get total sales for the previous month
    getSalesPrevMonth: () => api.get('/orders/reports/sales/prev-month'),
    
    // Get the number of times a book has been ordered from publishers
    getBookOrderCount: (isbn) => api.get(`/orders/reports/publisher-orders/${isbn}`),
};

// ==================== CUSTOMER API ====================
// Endpoints for customer registration
export const customerApi = {
    // Register a new customer account
    register: (data) => api.post('/customers', data)
};

// ==================== AUTH API ====================
// Endpoints for authentication
export const authApi = {
    // Login with username, password, and role (CUSTOMER or ADMIN)
    login: (data) => api.post('/auth/login', data)
};

// ==================== PUBLISHER ORDER API ====================
// Endpoints for managing publisher orders (admin only)
// Publisher orders are automatically created when book stock drops below threshold
export const publisherOrderApi = {
    // Get all pending publisher orders with book details
    // These are orders that have been auto-created but not yet confirmed
    getPending: () => api.get('/publisher-orders/pending'),
    
    // Confirm a publisher order
    // This changes the status from 'Pending' to 'Confirmed' and adds stock to the books
    confirm: (orderId) => api.post(`/publisher-orders/${orderId}/confirm`),
};

// ==================== PUBLISHER API ====================
// Endpoints for getting publisher data (used in admin book creation form)
export const publisherApi = {
    // Get all publishers for the dropdown in the add book form
    getAll: () => api.get('/publishers'),
};

// ==================== CATEGORY API ====================
// Endpoints for getting category data (used in admin book creation form)
export const categoryApi = {
    // Get all categories for the dropdown in the add book form
    getAll: () => api.get('/categories'),
};

// Export the base api instance for direct use when needed
export default api;
