export interface LineStatus {
  code: string;
  label: string;
  description: string | null;
}

export interface Line {
  uid: string;
  number: number;
  name: string;
  operator: string;
  colorHex: string;
  status: LineStatus;
  category: string;
}

export interface LineGroup {
  name: string;
  lines: Line[];
}

export interface LineStatusResponse {
  lastUpdated: string;
  groups: LineGroup[];
}

export interface ApiError {
  error: string;
  message: string;
}

const API_BASE = 'http://localhost:8081';

export async function fetchLineStatus(): Promise<LineStatusResponse> {
  const response = await fetch(`${API_BASE}/api/line-status`);

  if (!response.ok) {
    const errorBody: ApiError = await response.json().catch(() => ({
      error: 'UNKNOWN',
      message: 'Failed to fetch line status',
    }));
    throw new Error(errorBody.message);
  }

  return response.json();
}
