import { Card } from 'react-bootstrap'
import type { RateData } from '../model/RateData'
import { Line } from 'react-chartjs-2'
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend
} from 'chart.js'
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faNoteSticky } from '@fortawesome/free-solid-svg-icons';
import './RateCard.css'

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend)

type Props = {
    data: RateData
    history?: number[] // 7日間レートのサンプル
}

export default function RateCard({ data, history }: Props) {
    const chartData = {
        labels: history?.map((_, i) => `D-${6 - i}`) || [],
        datasets: [
            {
                label: `${data.currency}/JPY`,
                data: history || [],
                borderColor: 'rgba(0,123,255,0.8)', // ブルー系で統一
                backgroundColor: 'rgba(0,0,0,0)',
                tension: 0.3
            }
        ]
    }

    const chartOptions = {
        responsive: true,
        plugins: {
            legend: { display: false },
            tooltip: { enabled: true }
        },
        scales: {
            x: { display: false },
            y: { display: false }
        }
    }

    return (
        <Card className="mb-3 shadow-sm rounded-3 h-100">
            <Card.Header className="bg-secondary text-white">
                {data.currency} / JPY
            </Card.Header>

            <Card.Body>
                <Card.Text>
                    <strong>レート:</strong> {data.rate}<br />
                    <strong>前日差:</strong> {data.diff} ({data.diffPercent}%)<br />
                    <strong>7日平均:</strong> {data.avg7}
                </Card.Text>

                {history && history.length > 0 && (
                    <div style={{ height: '80px' }}>
                        <Line data={chartData} options={chartOptions} />
                    </div>
                )}

                <div className="ai-comment mt-2">
                    <FontAwesomeIcon icon={faNoteSticky} className="me-2" />
                    {data.aiSummary}
                </div>
            </Card.Body>
        </Card>
    )
}
