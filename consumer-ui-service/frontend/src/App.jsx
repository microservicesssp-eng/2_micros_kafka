import React, { useEffect, useState } from "react";

const REFRESH_INTERVAL_MS = 3000;

function formatDate(value) {
  return new Intl.DateTimeFormat("ru-RU", {
    dateStyle: "medium",
    timeStyle: "medium",
  }).format(new Date(value));
}

function App() {
  const [data, setData] = useState({ totalCount: 0, messages: [] });
  const [error, setError] = useState("");

  useEffect(() => {
    let isActive = true;

    async function loadMessages() {
      try {
        const response = await fetch("/api/messages");
        if (!response.ok) {
          throw new Error(`Сервер ответил с кодом ${response.status}`);
        }
        const result = await response.json();
        if (isActive) {
          setData(result);
          setError("");
        }
      } catch (loadError) {
        if (isActive) {
          setError(`Не удалось получить сообщения: ${loadError.message}`);
        }
      }
    }

    loadMessages();
    const intervalId = window.setInterval(loadMessages, REFRESH_INTERVAL_MS);

    return () => {
      isActive = false;
      window.clearInterval(intervalId);
    };
  }, []);

  return (
    <main className="page">
      <header className="topbar">
        <a className="brand" href="/">
          <span className="brand-mark" aria-hidden="true">K</span>
          <span>Kafka<span className="brand-light"> / монитор</span></span>
        </a>
        <span className="live-indicator"><span /> поток сообщений</span>
      </header>

      <section className="intro">
        <div>
          <p className="eyebrow">ДЕМОНСТРАЦИОННЫЙ ПРОЕКТ</p>
          <h1>Сообщения в реальном времени</h1>
          <p className="subtitle">
            Строки проходят через Kafka, сохраняются в MySQL и появляются здесь.
          </p>
        </div>
        <div className="kafka-art" aria-hidden="true">
          <span className="orbit orbit-one" />
          <span className="orbit orbit-two" />
          <span className="kafka-symbol">K</span>
        </div>
      </section>

      <section className="stats" aria-label="Сводка сообщений">
        <article className="stat-card">
          <span className="stat-icon purple" aria-hidden="true">≋</span>
          <div>
            <span className="stat-label">Записано в MySQL</span>
            <strong>{data.totalCount}</strong>
          </div>
        </article>
        <article className="stat-card">
          <span className="stat-icon green" aria-hidden="true">●</span>
          <div>
            <span className="stat-label">Обновление</span>
            <strong className="refresh-value">каждые 3 секунды</strong>
          </div>
        </article>
      </section>

      <section className="messages-panel">
        <div className="panel-heading">
          <div>
            <p className="eyebrow">MYSQL · ПОСЛЕДНИЕ 100 ЗАПИСЕЙ</p>
            <h2>Полученные строки</h2>
          </div>
          <span className="count-badge">{data.totalCount} всего</span>
        </div>

        {error && <p className="error-message" role="alert">{error}</p>}

        {!error && data.messages.length === 0 ? (
          <div className="empty-state">
            <span aria-hidden="true">⌛</span>
            <strong>Ожидаем первое сообщение</strong>
            <p>Producer отправит случайную строку в Kafka. Это обычно занимает несколько секунд.</p>
          </div>
        ) : (
          <ol className="message-list">
            {data.messages.map((message) => (
              <li className="message-row" key={message.id}>
              <span className="row-number">Строка {message.id}</span>
                <code>{message.content}</code>
                <time dateTime={message.receivedAt}>{formatDate(message.receivedAt)}</time>
              </li>
            ))}
          </ol>
        )}
        <p className="panel-footnote">
          Данные загружаются из MySQL через API Spring Boot. Новые записи сверху не теряются:
          отображаются последние 100 строк.
        </p>
      </section>

      <footer>Spring Boot · Apache Kafka · MySQL · React</footer>
    </main>
  );
}

export default App;
