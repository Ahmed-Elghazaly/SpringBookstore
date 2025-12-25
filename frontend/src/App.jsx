import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import {Toaster} from 'react-hot-toast';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Home from './pages/Home';
import Cart from './pages/Cart';
import OrderHistory from './pages/OrderHistory';
import AdminDashboard from './pages/AdminDashboard';
import AdminBooks from './pages/AdminBooks';
import AdminPublisherOrders from './pages/AdminPublisherOrders';
import Profile from "./pages/Profile.jsx";

// CustomerRoute is a wrapper component that protects customer-only pages.
// If the user is not logged in or is not a customer, they are redirected to login.
function CustomerRoute({children}) {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user || user.role !== 'CUSTOMER') return <Navigate to="/login"/>;
    return children;
}

// AdminRoute is a wrapper component that protects admin-only pages.
// If the user is not logged in or is not an admin, they are redirected to login.
function AdminRoute({children}) {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user || user.role !== 'ADMIN') return <Navigate to="/login"/>;
    return children;
}

function App() {
    return (<BrowserRouter>
        {/* Toaster provides toast notifications positioned at bottom-right */}
        <Toaster position="bottom-right"/>
        <Routes>
            {/* Public route - anyone can access the login page */}
            <Route path="/login" element={<Login/>}/>

            {/* ==================== CUSTOMER ROUTES ==================== */}
            
            {/* Home page - browse and search books */}
            <Route path="/" element={<CustomerRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <Home/>
                </div>
            </CustomerRoute>}/>

            {/* Shopping cart page */}
            <Route path="/cart" element={<CustomerRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <Cart/>
                </div>
            </CustomerRoute>}/>

            {/* Order history page - view past purchases */}
            <Route path="/orders" element={<CustomerRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <OrderHistory/>
                </div>
            </CustomerRoute>}/>

            {/* Profile page - edit personal information */}
            <Route path="/profile" element={<CustomerRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <Profile/>
                </div>
            </CustomerRoute>}/>

            {/* ==================== ADMIN ROUTES ==================== */}
            
            {/* Admin dashboard - view reports (top customers, top books, sales) */}
            <Route path="/admin" element={<AdminRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <AdminDashboard/>
                </div>
            </AdminRoute>}/>

            {/* Admin book management - add and edit books */}
            <Route path="/admin/books" element={<AdminRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <AdminBooks/>
                </div>
            </AdminRoute>}/>

            {/* Admin publisher orders - view and confirm pending orders to add stock */}
            <Route path="/admin/publisher-orders" element={<AdminRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <AdminPublisherOrders/>
                </div>
            </AdminRoute>}/>
        </Routes>
    </BrowserRouter>);
}

export default App;
