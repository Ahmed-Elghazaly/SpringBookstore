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
import AdminSettings from './pages/AdminSettings';
import Profile from "./pages/Profile.jsx";

// CustomerRoute protects customer-only pages
// Redirects to login if not logged in or not a customer
function CustomerRoute({children}) {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user || user.role !== 'CUSTOMER') return <Navigate to="/login"/>;
    return children;
}

// AdminRoute protects admin-only pages
// Redirects to login if not logged in or not an admin
function AdminRoute({children}) {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user || user.role !== 'ADMIN') return <Navigate to="/login"/>;
    return children;
}

function App() {
    return (<BrowserRouter>
        {/* Toast notifications appear at bottom-right */}
        <Toaster position="bottom-right"/>
        <Routes>
            {/* Public route - Login page */}
            <Route path="/login" element={<Login/>}/>

            {/* ==================== CUSTOMER ROUTES ==================== */}
            
            {/* Home - Browse and search books */}
            <Route path="/" element={<CustomerRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <Home/>
                </div>
            </CustomerRoute>}/>

            {/* Shopping Cart */}
            <Route path="/cart" element={<CustomerRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <Cart/>
                </div>
            </CustomerRoute>}/>

            {/* Order History */}
            <Route path="/orders" element={<CustomerRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <OrderHistory/>
                </div>
            </CustomerRoute>}/>

            {/* Customer Profile */}
            <Route path="/profile" element={<CustomerRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <Profile/>
                </div>
            </CustomerRoute>}/>

            {/* ==================== ADMIN ROUTES ==================== */}
            
            {/* Admin Dashboard - Reports */}
            <Route path="/admin" element={<AdminRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <AdminDashboard/>
                </div>
            </AdminRoute>}/>

            {/* Admin Book Management */}
            <Route path="/admin/books" element={<AdminRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <AdminBooks/>
                </div>
            </AdminRoute>}/>

            {/* Admin Publisher Orders */}
            <Route path="/admin/publisher-orders" element={<AdminRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <AdminPublisherOrders/>
                </div>
            </AdminRoute>}/>

            {/* Admin Settings - Manage Publishers & Categories */}
            <Route path="/admin/settings" element={<AdminRoute>
                <div className="min-h-screen bg-gray-50">
                    <Navbar/>
                    <AdminSettings/>
                </div>
            </AdminRoute>}/>
        </Routes>
    </BrowserRouter>);
}

export default App;
