package com.mainproject.config;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

/**
 * VoiceAssistantConfig.java
 * Backend for VoiceAssistant.java (BuyerDashboard voice assistant screen).
 *
 * FULLY ON GROQ NOW — one free API key, no Google Cloud, no billing/card
 * needed.
 *
 * Pipeline:
 * recordAudio() -> captures mic audio (javax.sound.sampled), wraps it as a WAV
 * file
 * transcribe() -> sends the WAV to Groq's Whisper API (speech-to-text)
 * askAssistant() -> sends transcript to Groq's LLM chat API for an AI answer
 * speak() -> speaks the AI answer using the OS's built-in TTS engine
 *
 * ================= SETUP =================
 * 1) org.json dependency (Maven):
 * <dependency>
 * <groupId>org.json</groupId>
 * <artifactId>json</artifactId>
 * <version>20240303</version>
 * </dependency>
 *
 * 2) GROQ_API_KEY -> free key, no card required:
 * - Go to https://console.groq.com
 * - Sign up with email or Google (no card, no phone verification)
 * - Left menu -> "API Keys" -> "Create API Key" -> copy it (shown only once)
 *
 * 3) Paste that key below in GROQ_API_KEY (better: read from an environment
 * variable
 * with System.getenv("GROQ_API_KEY") instead of hardcoding it).
 * ===========================================
 */
public class VoiceAssistantConfig {

    // Force console output to UTF-8 so Hindi/Marathi transcripts and special
    // characters
    // (like apostrophes) print correctly instead of showing as "?" or "I?m".
    static {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
            System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));
        } catch (Exception ignored) {
        }
    }

    // ---------- API KEY ----------

    private static final String GROQ_API_KEY = "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";

    private static final String GROQ_TRANSCRIBE_URL = "https://api.groq.com/openai/v1/audio/transcriptions";
    private static final String GROQ_CHAT_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final String WHISPER_MODEL = "whisper-large-v3";
    private static final String CHAT_MODEL = "openai/gpt-oss-120b";

    // ---------- AUDIO SETTINGS ----------
    private static final float SAMPLE_RATE = 16000;
    private static final int SAMPLE_SIZE_BITS = 16;
    private static final int CHANNELS = 1;

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    // =====================================================
    // LANGUAGE
    // =====================================================

    public enum Language {
        ENGLISH("en"),
        HINDI("hi"),
        MARATHI("mr");

        private final String whisperCode;

        Language(String whisperCode) {
            this.whisperCode = whisperCode;
        }

        public String getWhisperCode() {
            return whisperCode;
        }
    }

    private static Language selectedLanguage = Language.ENGLISH;

    public static void setLanguage(Language language) {
        selectedLanguage = language;
    }

    public static Language getLanguage() {
        return selectedLanguage;
    }

    // =====================================================
    // STEP 1 — RECORD AUDIO FROM MICROPHONE (returns a proper WAV file)
    // =====================================================

    /**
     * Records audio (16-bit, mono, 16kHz) from the default microphone for the
     * given number of seconds and returns it as a complete WAV file (with header),
     * ready to upload to Groq's Whisper API.
     */
    public static byte[] recordAudio(int seconds) throws Exception {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_BITS, CHANNELS, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            throw new Exception("Microphone line not supported on this system.");
        }

        TargetDataLine micLine = (TargetDataLine) AudioSystem.getLine(info);
        micLine.open(format);
        micLine.start();

        ByteArrayOutputStream rawOut = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        long endTime = System.currentTimeMillis() + (seconds * 1000L);

        while (System.currentTimeMillis() < endTime) {
            int bytesRead = micLine.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                rawOut.write(buffer, 0, bytesRead);
            }
        }

        micLine.stop();
        micLine.close();

        byte[] rawPcm = rawOut.toByteArray();

        // Wrap raw PCM bytes into a proper WAV file (adds the header Whisper needs)
        ByteArrayOutputStream wavOut = new ByteArrayOutputStream();
        try (AudioInputStream audioInputStream = new AudioInputStream(
                new ByteArrayInputStream(rawPcm), format, rawPcm.length / format.getFrameSize())) {
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavOut);
        }

        return wavOut.toByteArray();
    }

    // =====================================================
    // STEP 2 — SPEECH TO TEXT (Groq Whisper API)
    // =====================================================

    /**
     * Sends a recorded WAV file to Groq's Whisper API and returns the transcript.
     */
    public static String transcribe(byte[] wavAudioBytes) throws Exception {
        String boundary = "----VoiceAssistantBoundary" + UUID.randomUUID();

        List<byte[]> multipartParts = new ArrayList<>();

        // "model" field
        multipartParts.add(textPart(boundary, "model", WHISPER_MODEL));

        // "language" field (Whisper ISO-639-1 code, e.g. "en", "hi", "mr")
        multipartParts.add(textPart(boundary, "language", selectedLanguage.getWhisperCode()));

        // "file" field (the actual audio)
        ByteArrayOutputStream filePart = new ByteArrayOutputStream();
        filePart.write(("--" + boundary + "\r\n").getBytes(StandardCharsets.UTF_8));
        filePart.write(("Content-Disposition: form-data; name=\"file\"; filename=\"recording.wav\"\r\n")
                .getBytes(StandardCharsets.UTF_8));
        filePart.write("Content-Type: audio/wav\r\n\r\n".getBytes(StandardCharsets.UTF_8));
        filePart.write(wavAudioBytes);
        filePart.write("\r\n".getBytes(StandardCharsets.UTF_8));
        multipartParts.add(filePart.toByteArray());

        // closing boundary
        multipartParts.add(("--" + boundary + "--\r\n").getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream fullBody = new ByteArrayOutputStream();
        for (byte[] part : multipartParts) {
            fullBody.write(part);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GROQ_TRANSCRIBE_URL))
                .header("Authorization", "Bearer " + GROQ_API_KEY)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(fullBody.toByteArray()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Speech-to-Text error (" + response.statusCode() + "): " + response.body());
        }

        JSONObject root = new JSONObject(response.body());
        return root.optString("text", "").trim();
    }

    private static byte[] textPart(String boundary, String fieldName, String value) {
        String part = "--" + boundary + "\r\n"
                + "Content-Disposition: form-data; name=\"" + fieldName + "\"\r\n\r\n"
                + value + "\r\n";
        return part.getBytes(StandardCharsets.UTF_8);
    }

    // =====================================================
    // STEP 3 — ASK AI (Groq LLM chat)
    // =====================================================

    /**
     * Sends the transcribed question to Groq's chat API and returns a short helpful
     * answer,
     * scoped to the AgriLink farm marketplace context.
     */
    public static String askAssistant(String userQuery) throws Exception {
        String languageInstruction = switch (selectedLanguage) {
            case HINDI -> "Reply in Hindi.";
            case MARATHI -> "Reply in Marathi.";
            default -> "Reply in English.";
        };

        String systemPrompt = "You are AgriVerse AI, AgriLink's farm marketplace voice assistant. "
                + "Answer briefly (2-3 sentences max) and helpfully about crops, prices, orders, or farmers. "
                + languageInstruction;

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", systemPrompt));
        messages.put(new JSONObject().put("role", "user").put("content", userQuery));

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", CHAT_MODEL);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.6);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GROQ_CHAT_URL))
                .header("Authorization", "Bearer " + GROQ_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new Exception("Groq AI error (" + response.statusCode() + "): " + response.body());
        }

        JSONObject root = new JSONObject(response.body());
        JSONArray choices = root.getJSONArray("choices");
        JSONObject firstChoice = choices.getJSONObject(0);
        JSONObject message = firstChoice.getJSONObject("message");
        return message.getString("content").trim();
    }

    // =====================================================
    // STEP 4 — TEXT TO SPEECH (OS-level, no extra API key needed)
    // =====================================================

    /**
     * Speaks the given text out loud using the operating system's built-in TTS.
     * Windows -> SAPI (via PowerShell), macOS -> "say", Linux -> "espeak".
     */
    public static void speak(String text) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;

            if (os.contains("win")) {
                // Normalize "smart"/curly apostrophes (’ ‘ ´ `) to a plain straight apostrophe
                // first — LLMs often return curly ones, which would otherwise slip past the
                // escaping below and still break the PowerShell string.
                String normalized = text
                        .replace('\u2019', '\'')
                        .replace('\u2018', '\'')
                        .replace('\u00B4', '\'')
                        .replace('`', '\'');

                // PowerShell single-quoted strings need '' to escape a literal single quote.
                String escapedForPs = normalized.replace("'", "''");

                // Explicitly force Volume to 100 and route to the default audio device —
                // SAPI sometimes starts at a low/zero volume in headless/service contexts.
                String psScript = "Add-Type -AssemblyName System.Speech; "
                        + "$s = New-Object System.Speech.Synthesis.SpeechSynthesizer; "
                        + "$s.SetOutputToDefaultAudioDevice(); "
                        + "$s.Volume = 100; "
                        + "$s.Rate = 0; "
                        + "$s.Speak('" + escapedForPs + "');";

                // -EncodedCommand sidesteps ALL command-line quoting problems (apostrophes,
                // quotes, punctuation, unicode from the AI's reply) — the script is passed
                // as Base64 UTF-16LE instead of being parsed as raw command-line text.
                byte[] utf16Bytes = psScript.getBytes(StandardCharsets.UTF_16LE);
                String encodedCommand = Base64.getEncoder().encodeToString(utf16Bytes);

                pb = new ProcessBuilder(
                        "powershell.exe", "-NoProfile", "-ExecutionPolicy", "Bypass",
                        "-EncodedCommand", encodedCommand);

            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("say", text);

            } else {
                // Linux — requires espeak installed (sudo apt install espeak)
                pb = new ProcessBuilder("espeak", text);
            }

            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Print whatever the process outputs so failures are visible in the console
            // instead of failing silently with no sound and no clue why.
            try (java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[TTS] " + line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("TTS process exited with code " + exitCode + " — check [TTS] logs above.");
            }

        } catch (Exception e) {
            System.err.println("TTS failed (voice output skipped): " + e.getMessage());
        }
    }
}