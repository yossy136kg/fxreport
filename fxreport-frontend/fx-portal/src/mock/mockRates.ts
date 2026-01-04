import type { RateData } from '../model/RateData'

export const mockRates: RateData[] = [
  {
    currency: 'USD',
    rate: 157.2,
    diff: 0.4,
    diffPercent: 0.25,
    avg7: 156.8,
    aiSummary: 'ドル円は前日比で小幅に上昇しました。'
  },
  {
    currency: 'EUR',
    rate: 171.5,
    diff: -0.3,
    diffPercent: -0.17,
    avg7: 172.1,
    aiSummary: 'ユーロ円はやや調整局面です。'
  },
  {
    currency: 'GBP',
    rate: 196.3,
    diff: 0.8,
    diffPercent: 0.41,
    avg7: 195.5,
    aiSummary: 'ポンド円は強めに推移しています。'
  },
  {
    currency: 'AUD',
    rate: 108.7,
    diff: -0.2,
    diffPercent: -0.18,
    avg7: 108.9,
    aiSummary: '豪ドル円はほぼ横ばいです。'
  },
  {
    currency: 'NZD',
    rate: 96.4,
    diff: 0.1,
    diffPercent: 0.10,
    avg7: 96.3,
    aiSummary: 'ニュージー円はわずかに上昇。'
  },
  {
    currency: 'CHF',
    rate: 172.9,
    diff: -0.5,
    diffPercent: -0.29,
    avg7: 173.2,
    aiSummary: 'スイスフラン円はやや下落しました。'
  },
  {
    currency: 'CAD',
    rate: 124.6,
    diff: 0.3,
    diffPercent: 0.24,
    avg7: 124.3,
    aiSummary: 'カナダドル円は穏やかに上昇。'
  },
  {
    currency: 'CNY',
    rate: 21.9,
    diff: 0.0,
    diffPercent: 0.00,
    avg7: 21.9,
    aiSummary: '人民元円は安定しています。'
  }
]
