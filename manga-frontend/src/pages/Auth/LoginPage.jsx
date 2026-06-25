import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";

export default function LoginPage() {
  const navigate = useNavigate();
  const { login } = useAuth();

  // State für das Formular (nur die Felder, die für den Login nötig sind)
  const [form, setForm] = useState({ usernameOrEmail: "", password: "" });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [token, setToken] = useState(localStorage.getItem("token") || null);
  const [showPassword, setShowPassword] = useState(false);

  function togglePasswordVisibility() {
    setShowPassword((prev) => !prev);
  }

  function logout() {
    localStorage.removeItem("token");
    setToken(null);
  }
  async function handleSubmit(event) {
    event.preventDefault();
    setError("");

    // Validierung: Passwortlänge prüfen (Client-seitiger Vorab-Check)
    if (form.password.length < 6) {
      setError("Passwort muss mindestens 6 Zeichen lang sein.");
      return;
    }

    setIsLoading(true);

    try {
      // Nutzt die login-Funktion aus dem AuthContext
      await login(form.usernameOrEmail, form.password);
      navigate("/mangas");
    } catch (err) {
      setError(err.message || "Login fehlgeschlagen");
      alert("Login fehlgeschlagen, versuche es erneut.");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <section className="auth-page">
      <h2>Login</h2>

      {error && <div style={{ color: "red", marginBottom: 12 }}>{error}</div>}

      <form onSubmit={handleSubmit}>
        {/* Username / E-Mail Feld */}
        <div>
          <label htmlFor="usernameOrEmail">Username oder E-Mail</label>
          <input
            id="usernameOrEmail"
            type="text"
            value={form.usernameOrEmail}
            onChange={(event) =>
              setForm((current) => ({ ...current, usernameOrEmail: event.target.value }))
            }
            placeholder="admin oder admin@manga.local"
            required
          />
        </div>

        {/* Passwort Feld */}
        <div>
          <label htmlFor="password">Passwort</label>
          <div style={{ display: "flex", gap: "8px", alignItems: "center" }}>
            <input
              id="password"
              // Typ ändert sich dynamisch, damit man das Passwort sehen kann
              type={showPassword ? "text" : "password"}
              value={form.password}
              onChange={(event) =>
                setForm((current) => ({ ...current, password: event.target.value }))
              }
              placeholder="Mindestens 6 Zeichen"
              required
            />
            <button type="button" onClick={togglePasswordVisibility}>
              {showPassword ? "Ausblenden" : "Anzeigen"}
            </button>
          </div>
        </div>

        {/* Einziger, korrekter Submit Button */}
        <button type="submit" disabled={isLoading} style={{ marginTop: 12 }}>
          {isLoading ? "Logge ein..." : "Einloggen"}
        </button>
      </form>

      <p>
        Noch kein Account? <Link to="/register">Jetzt registrieren</Link>
      </p>

      <div style={{ marginTop: 16 }}>
        <strong>Test-Accounts:</strong>
        <div>Admin: admin / admin123</div>
        <div>User: hans / welcome123</div>
      </div>
    </section>
  );
}