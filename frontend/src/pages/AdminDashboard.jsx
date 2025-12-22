import { useEffect, useState } from 'react';
import { reportApi } from '../api/client';

export default function AdminDashboard() {
    const [activeTab, setActiveTab] = useState('customers');
    const [data, setData] = useState([]);

    const loadReport = async () => {
        let res;
        if (activeTab === 'customers') res = await reportApi.getTopCustomers();
        if (activeTab === 'books') res = await reportApi.getTopBooks();
        // For date/month, you'd add inputs. Simply logging for demo
        setData(res ? res.data : []);
    };

    useEffect(() => { loadReport(); }, [activeTab]);

    return (
        <div className="max-w-6xl mx-auto px-4 py-8">
            <div className="flex justify-between items-center mb-8">
                <h1 className="text-3xl font-bold text-gray-900">Admin Reports</h1>
                <div className="flex bg-white rounded-lg p-1 border border-gray-200">
                    <button
                        onClick={() => setActiveTab('customers')}
                        className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'customers' ? 'bg-brand-100 text-brand-700' : 'text-gray-500 hover:text-gray-900'}`}
                    >
                        Top Customers
                    </button>
                    <button
                        onClick={() => setActiveTab('books')}
                        className={`px-4 py-2 rounded-md text-sm font-medium ${activeTab === 'books' ? 'bg-brand-100 text-brand-700' : 'text-gray-500 hover:text-gray-900'}`}
                    >
                        Top Selling Books
                    </button>
                </div>
            </div>

            <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
        <pre className="p-6 text-sm text-gray-700 font-mono bg-gray-50">
          {JSON.stringify(data, null, 2)}
        </pre>
                {/* You can map this nicely to a table like OrderHistory */}
            </div>
        </div>
    );
}