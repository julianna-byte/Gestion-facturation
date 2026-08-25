const API_URL = "http://localhost:8081/api/auth";

export async function login(identifiant, motdepasse) {
    const response = await fetch(`${API_URL}/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            identifiant: identifiant,
            motdepasse: motdepasse,
        }),
    });

    if (!response.ok) {
        throw new Error("Identifiants invalides");
    }

    return await response.json();
}
