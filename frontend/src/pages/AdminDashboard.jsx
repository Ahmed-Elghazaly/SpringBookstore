import {useEffect, useState} from 'react';
import {reportApi} from '../api/client';

export default function AdminDashboard() {
    const [activeTab, setActiveTab] = useState('customers');
    const [data, setData] = useState([]);

    // SALES REPORT STATES (Split to avoid conflict)
    const [salesDate, setSalesDate] = useState('');
    const [dateSalesAmount, setDateSalesAmount] = useState(null);      // <--- State for specific date
    const [prevMonthSalesAmount, setPrevMonthSalesAmount] = useState(null); // <--- State for previous month

    const [bookIsbn, setBookIsbn] = useState('');
    const [orderCount, setOrderCount] = useState(null);

    const loadReport = async () => {
        try {
            let res;
            if (activeTab === 'customers') res = await reportApi.getTopCustomers();
            if (activeTab === 'books') res = await reportApi.getTopBooks();
            setData(res ? res.data : []);
        } catch (err) {
            console.error("Failed to load report", err);
        }
    };

    useEffect(() => {
        loadReport();
    }, [activeTab]);

    const handleDateSales = async () => {
        if (!salesDate) return;
        try {
            const res = await reportApi.getSalesByDate(salesDate);
            // Update ONLY the specific date state
            setDateSalesAmount(res.data.totalSales);
        } catch (err) {
            console.error(err);
        }
    };

    const handlePrevMonthSales = async () => {
        try {
            const res = await reportApi.getSalesPrevMonth();
            // Update ONLY the previous month state
            setPrevMonthSalesAmount(res.data.totalSales);
        } catch (err) {
            console.error(err);
        }
    };

    const handleBookOrderCount = async () => {
        if (!bookIsbn) return;
        try {
            const res = await reportApi.getBookOrderCount(bookIsbn);
            setOrderCount(res.data);
        } catch (err) {
            console.error(err);
        }
    };

    return (<div className="max-w-6xl mx-auto px-4 py-8">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">Admin Dashboard</h1>

        {/* Tab Navigation */}
        <div className="flex bg-white rounded-lg p-1 border border-gray-200 mb-8">
            <button onClick={() => setActiveTab('customers')}
                    className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'customers' ? 'bg-brand-100 text-brand-700' : 'text-gray-500'}`}>Top
                Customers
            </button>
            <button onClick={() => setActiveTab('books')}
                    className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'books' ? 'bg-brand-100 text-brand-700' : 'text-gray-500'}`}>Top
                Selling Books
            </button>
            <button onClick={() => setActiveTab('sales')}
                    className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'sales' ? 'bg-brand-100 text-brand-700' : 'text-gray-500'}`}>Sales
                Reports
            </button>
            <button onClick={() => setActiveTab('orders')}
                    className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'orders' ? 'bg-brand-100 text-brand-700' : 'text-gray-500'}`}>Publisher
                Orders
            </button>
        </div>

        {/* Top Customers Table */}
        {activeTab === 'customers' && (<div className="bg-white rounded-xl shadow-sm border overflow-hidden">
            <table className="w-full">
                <thead className="bg-gray-50">
                <tr>
                    <th className="p-4 text-left">Customer Name</th>
                    <th className="p-4 text-left">Total Spent</th>
                </tr>
                </thead>
                <tbody>
                {data.map((row, i) => (<tr key={i} className="border-t">
                    <td className="p-4">{row.firstName} {row.lastName}</td>
                    <td className="p-4 font-bold text-green-600">${row.totalSpent}</td>
                </tr>))}
                </tbody>
            </table>
        </div>)}

        {/* Top Books Table */}
        {activeTab === 'books' && (<div className="bg-white rounded-xl shadow-sm border overflow-hidden">
            <table className="w-full">
                <thead className="bg-gray-50">
                <tr>
                    <th className="p-4 text-left">Book Title</th>
                    <th className="p-4 text-left">ISBN</th>
                    <th className="p-4 text-left">Total Sold</th>
                </tr>
                </thead>
                <tbody>
                {data.map((row, i) => (<tr key={i} className="border-t">
                    <td className="p-4 font-bold">{row.title}</td>
                    <td className="p-4 text-gray-500 font-mono">{row.isbn}</td>
                    <td className="p-4 font-bold text-brand-600">{row.totalSold}</td>
                </tr>))}
                </tbody>
            </table>
        </div>)}

        {/* Sales Reports */}
        {activeTab === 'sales' && (<div className="space-y-6">
            {/* Report 1: Specific Date */}
            <div className="bg-white p-6 rounded-xl shadow-sm border">
                <h3 className="font-bold mb-4">Sales by Specific Date</h3>
                <div className="flex gap-4">
                    <input type="date" value={salesDate} onChange={(e) => setSalesDate(e.target.value)}
                           className="p-3 border rounded-lg flex-1"/>
                    <button onClick={handleDateSales}
                            className="bg-brand-600 text-white px-6 py-3 rounded-lg font-bold">Get Sales
                    </button>
                </div>
                {/* Only show dateSalesAmount here */}
                {dateSalesAmount !== null && (
                    <div className="mt-4 text-2xl font-bold text-green-600">Total: ${dateSalesAmount}</div>)}
            </div>

            {/* Report 2: Previous Month */}
            <div className="bg-white p-6 rounded-xl shadow-sm border">
                <h3 className="font-bold mb-4">Sales Previous Month</h3>
                <button onClick={handlePrevMonthSales}
                        className="bg-brand-600 text-white px-6 py-3 rounded-lg font-bold">Get Previous Month
                    Sales
                </button>
                {/* Only show prevMonthSalesAmount here */}
                {prevMonthSalesAmount !== null && (
                    <div className="mt-4 text-2xl font-bold text-green-600">Total:
                        ${prevMonthSalesAmount}</div>)}
            </div>
        </div>)}

        {/* Publisher Orders Count */}
        {activeTab === 'orders' && (<div className="bg-white p-6 rounded-xl shadow-sm border">
            <h3 className="font-bold mb-4">Publisher Order Count for Book</h3>
            <div className="flex gap-4">
                <input type="text" placeholder="Enter ISBN" value={bookIsbn}
                       onChange={(e) => setBookIsbn(e.target.value)} className="p-3 border rounded-lg flex-1"/>
                <button onClick={handleBookOrderCount}
                        className="bg-brand-600 text-white px-6 py-3 rounded-lg font-bold">Get Count
                </button>
            </div>
            {orderCount !== null && (
                <div className="mt-4 text-2xl font-bold text-brand-600">Times Ordered: {orderCount}</div>)}
        </div>)}
    </div>);
}