package app.ddulce.supabasenotes

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header

val ngrokClient = HttpClient(OkHttp) {
    defaultRequest {
        header("ngrok-skip-browser-warning", "true")
    }
}

val supabase = createSupabaseClient(
    supabaseUrl = "https://thorough-strangely-wahoo.ngrok-free.app",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZS1kZW1vIiwicm9sZSI6ImFub24iLCJleHAiOjE5ODM4MTI5OTZ9.CRXP1A7WOeoJeXxjNni43kdQwgnWNReilDMblYTn_I0"
) {
    httpEngine = ngrokClient.engine
    install(Postgrest)
}