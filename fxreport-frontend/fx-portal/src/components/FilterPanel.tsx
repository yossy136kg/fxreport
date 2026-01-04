import { Form, Row, Col } from 'react-bootstrap'

type Props = {
  currencies: string[]
  selectedCurrencies: string[]
  onToggle: (currency: string) => void
}

export default function FilterPanel({
  currencies,
  selectedCurrencies,
  onToggle
}: Props) {
  return (
    <Form className="mb-4">
      {/* チェックボックスを横並びで折り返し */}
      <Row>
        {currencies.map(c => (
          <Col xs="auto" key={c} className="mb-2">
            <Form.Check
              type="checkbox"
              label={c}
              checked={selectedCurrencies.includes(c)}
              onChange={() => onToggle(c)}
              inline
            />
          </Col>
        ))}
      </Row>
    </Form>
  )
}
