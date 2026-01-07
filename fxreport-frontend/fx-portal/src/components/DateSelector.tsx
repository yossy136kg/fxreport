import { Form, Button, InputGroup } from 'react-bootstrap'

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
            <Form.Label>日付を選択</Form.Label>
            <div style={{ maxWidth: '30%' }}>      <InputGroup>
                <Button variant="outline-secondary" onClick={handlePrev}>
                    ◀
                </Button>
                <Form.Control
                    type="date"
                    value={selectedDate}
                    onChange={(e) => onChange(e.target.value)}
                />
                <Button variant="outline-secondary" onClick={handleNext}>
                    ▶
                </Button>
            </InputGroup>
            </div>
        </Form.Group >
    )
}
