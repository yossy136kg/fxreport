import { Form, Button } from 'react-bootstrap'
import './DateSelector.css'

export default function DateSelector({
    selectedDate,
    onChange
}: {
    selectedDate: string
    onChange: (newDate: string) => void
}) {
    const handlePrev = () => {
        const newDate = new Date(selectedDate)
        newDate.setDate(newDate.getDate() - 1)
        onChange(newDate.toISOString().slice(0, 10))
    }

    const handleNext = () => {
        const newDate = new Date(selectedDate)
        newDate.setDate(newDate.getDate() + 1)
        onChange(newDate.toISOString().slice(0, 10))
    }

    return (
        <Form.Group controlId="dateSelect" className="mb-3">
            <div className="date-selector">
                <Button
                    variant="outline-secondary"
                    onClick={handlePrev}
                    className="date-btn"
                >
                    ◀
                </Button>

                <Form.Control
                    type="date"
                    value={selectedDate}
                    onChange={(e) => onChange(e.target.value)}
                    className="date-input"
                />

                <Button
                    variant="outline-secondary"
                    onClick={handleNext}
                    className="date-btn"
                >
                    ▶
                </Button>
            </div>
        </Form.Group>
    )
}
