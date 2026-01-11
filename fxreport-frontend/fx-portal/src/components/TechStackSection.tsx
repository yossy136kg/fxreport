import { useState } from "react";

export const TechStackSection = () => {
    const [open, setOpen] = useState(false);
    return (
        <>
            <h6
                className="mt-2 mb-3 d-flex align-items-center"
                style={{ cursor: "pointer" }}
                onClick={() => setOpen(!open)}
            >
                ・技術スタック（Tech Stack）
                <span className="ms-2">
                    {open ? "▲" : "▼"}
                </span>
            </h6>
            {open && (
                <div className="row mt-3">
                    <div className="col-md-6 mb-3">
                        <h6>バックエンド</h6>
                        <ul className="mb-0">
                            <li>Java 21</li>
                            <li>Spring Boot</li>
                            <li>Gemini API（AI連携）</li>
                        </ul>
                    </div>

                    <div className="col-md-6 mb-3">
                        <h6>フロントエンド</h6>
                        <ul className="mb-0">
                            <li>React</li>
                            <li>TypeScript</li>
                        </ul>
                    </div>
                </div>
            )}
        </>
    );
};
