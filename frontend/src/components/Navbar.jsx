import {Link, useNavigate} from 'react-router-dom';
import api from '../api/client';
import {BookMarked, BookOpen, LayoutDashboard, LogOut, Package, Settings, ShoppingCart, User} from 'lucide-react';

export default function Navbar() {
    const navigate = useNavigate();
    // Get the current user from localStorage (set during login)
    const user = JSON.parse(localStorage.getItem('user'));

    // Handles user logout:
    // 1. Clears the customer's cart via API (so they start fresh next login)
    // 2. Removes user data from localStorage
    // 3. Redirects to the login page
    const handleLogout = async () => {
        const customerId = localStorage.getItem('customerId');
        if (customerId) {
            try {
                // FIX: Backend path is DELETE /api/cart/{customerId}
                // (No /clear at the end)
                await api.delete(`/cart/${customerId}`);
            } catch (err) {
                console.error("Failed to clear cart on logout:", err);
            }
        }
        localStorage.removeItem('user');
        localStorage.removeItem('customerId');
        navigate('/login');
    };

    // Don't render the navbar if no user is logged in
    if (!user) return null;

    return (<nav className="bg-white border-b border-gray-200 sticky top-0 z-50">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    {/* Logo/Brand - clicking navigates to home (different for admin vs customer) */}
                    <div
                        className="flex items-center cursor-pointer"
                        onClick={() => navigate(user.role === 'ADMIN' ? '/admin' : '/')}
                    >
                        <span className="text-2xl font-bold text-brand-600">GoldenBooks</span>
                        {/* Show ADMIN badge for admin users */}
                        {user.role === 'ADMIN' && (<span
                                className="ml-2 px-2 py-0.5 rounded text-xs bg-red-100 text-red-600 font-bold border border-red-200">
                                ADMIN
                            </span>)}
                    </div>

                    {/* Navigation Links */}
                    <div className="flex items-center space-x-4">

                        {/* ==================== CUSTOMER LINKS ==================== */}
                        {user.role === 'CUSTOMER' && (<>
                                {/* Browse books link */}
                                <Link
                                    to="/"
                                    className="text-gray-600 hover:text-brand-600 font-medium flex items-center gap-1 px-3 py-2 rounded-lg hover:bg-gray-100"
                                >
                                    <BookOpen size={18}/> Browse
                                </Link>

                                {/* Order history link */}
                                <Link
                                    to="/orders"
                                    className="text-gray-600 hover:text-brand-600 font-medium px-3 py-2 rounded-lg hover:bg-gray-100"
                                >
                                    My Orders
                                </Link>

                                {/* Profile link */}
                                <Link
                                    to="/profile"
                                    className="text-gray-600 hover:text-brand-600 font-medium flex items-center gap-1 px-3 py-2 rounded-lg hover:bg-gray-100"
                                >
                                    <User size={18}/> Profile
                                </Link>

                                {/* Shopping cart link */}
                                <Link
                                    to="/cart"
                                    className="relative text-gray-600 hover:text-brand-600 p-2 rounded-lg hover:bg-gray-100"
                                >
                                    <ShoppingCart size={24}/>
                                </Link>
                            </>)}

                        {/* ==================== ADMIN LINKS ==================== */}
                        {user.role === 'ADMIN' && (<>
                                {/* Reports dashboard link */}
                                <Link
                                    to="/admin"
                                    className="text-gray-600 hover:text-brand-600 font-medium flex items-center gap-1 px-3 py-2 rounded-lg hover:bg-gray-100"
                                >
                                    <LayoutDashboard size={18}/> Reports
                                </Link>

                                {/* Book management link */}
                                <Link
                                    to="/admin/books"
                                    className="text-gray-600 hover:text-brand-600 font-medium flex items-center gap-1 px-3 py-2 rounded-lg hover:bg-gray-100"
                                >
                                    <BookMarked size={18}/> Books
                                </Link>

                                {/* Publisher orders link */}
                                <Link
                                    to="/admin/publisher-orders"
                                    className="text-gray-600 hover:text-brand-600 font-medium flex items-center gap-1 px-3 py-2 rounded-lg hover:bg-gray-100"
                                >
                                    <Package size={18}/> Orders
                                </Link>

                                {/* Settings link - Manage Publishers & Categories */}
                                <Link
                                    to="/admin/settings"
                                    className="text-gray-600 hover:text-brand-600 font-medium flex items-center gap-1 px-3 py-2 rounded-lg hover:bg-gray-100"
                                >
                                    <Settings size={18}/> Settings
                                </Link>
                            </>)}

                        {/* Divider */}
                        <div className="h-6 w-px bg-gray-300 mx-2"></div>

                        {/* User info and logout */}
                        <div className="flex items-center gap-3">
                            {/* Display the user's name */}
                            <span className="text-sm font-semibold text-gray-700 hidden sm:block">
                                {user.name}
                            </span>
                            {/* Logout button */}
                            <button
                                onClick={handleLogout}
                                className="text-red-500 hover:bg-red-50 p-2 rounded-full"
                                title="Logout"
                            >
                                <LogOut size={20}/>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </nav>);
}
