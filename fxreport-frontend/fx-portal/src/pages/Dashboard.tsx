import { useEffect, useState } from 'react'
import { Container, Row, Col } from 'react-bootstrap'
import RateCard from '../components/RateCard'
import FilterPanel from '../components/FilterPanel'
import DateSelector from '../components/DateSelector'
import { TechStackSection } from "../components/TechStackSection";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import { faNoteSticky } from '@fortawesome/free-solid-svg-icons'
import type { RateData } from '../model/RateData'
import '../components/RateCard.css'

export default function Dashboard() {
    const [open, setOpen] = useState(false);
    const [rates, setRates] = useState<RateData[]>([])
    const currencies = Array.from(new Set(rates.map(r => r.currency)))
    const [selectedCurrencies, setSelectedCurrencies] = useState<string[]>(currencies)
    const d = new Date();
    d.setDate(d.getDate() - 1);
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    const formatted = `${yyyy}-${mm}-${dd}`;
    const [selectedDate, setSelectedDate] = useState<string>(formatted)
    const [aiReport, setAiReport] = useState("読み込み中...")
    const handleToggle = (currency: string) => {
        setSelectedCurrencies(prev =>
            prev.includes(currency)
                ? prev.filter(c => c !== currency)
                : [...prev, currency]
        )
    }

    const visibleRates = rates.filter(r =>
        selectedCurrencies.includes(r.currency)
    )

    useEffect(() => {
        const uniqueCurrencies = Array.from(new Set(rates.map(r => r.currency)))
        setSelectedCurrencies(uniqueCurrencies)
    }, [rates])

    useEffect(() => {
        const fetchAiReport = async () => {
            try {
                const res = await fetch(`/api/ai/report?date=${selectedDate}`);

                if (res.ok) {
                    const text = await res.text();
                    setAiReport(text || "（コメントなし）");
                } else {
                    setAiReport("（コメント取得に失敗しました）");
                }
            } catch (err) {
                setAiReport("（エラーが発生しました）");
            }
        };

        fetchAiReport();
    }, [selectedDate]);

    useEffect(() => {
        const loadSummary = async () => {
            try {
                const res = await fetch(`/api/fx/summary?date=${selectedDate}`);
                const data = res.ok ? await res.json() : [];
                setRates(data);
            } catch {
                setRates([]);
            }
        };
        loadSummary();
    }, [selectedDate]);

    return (
        <Container className="mt-4">
            {/* 画面タイトル */}
            <h1 className="mb-4">為替AI日次レポート</h1>

            <p className="mb-4">
                為替データを可視化・分析するためのフルスタック Web アプリケーションです。<br />
                バックエンドは <strong>Spring Boot（Java 21）</strong>、
                フロントエンドは <strong>React（Vite）</strong> で構成されています。
            </p>
            <TechStackSection />
            
            {/* 日時選択 */}
            <DateSelector
                selectedDate={selectedDate}
                onChange={(newDate) => setSelectedDate(newDate)}
            />

            {/* AI日次レポート */}
            <div className="ai-comment mt-2 mb-2">
                <FontAwesomeIcon icon={faNoteSticky} className="me-2" />
                {aiReport}
            </div>

            {/* フィルター */}
            <FilterPanel
                currencies={currencies}
                selectedCurrencies={selectedCurrencies}
                onToggle={handleToggle}
            />

            {/* カード */}
            <Row className="g-3 mb-4">
                {visibleRates.map(rate => (
                    <Col xs={12} md={6} lg={4} key={rate.currency}>
                        <RateCard
                            data={rate}
                            history={rate.history}
                        />
                    </Col>
                ))}
            </Row>

            {/* コピーライト */}
            <footer className="text-center text-muted py-3">
                © TECHGATE.WORK
            </footer>
        </Container>
    )
}
