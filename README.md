# MCP-Server

## FAQs

### How does the MCP Client know what to use?

The MCP client orchestrates interactions between users, the model (LLM), and the MCP server. The decision-making is distributed across **three actors**:

#### 🧠 1. The Model (LLM) — Decides **which _tools_ to call**
- Analyzes the user's intent and autonomously chooses tools.
- Tools are exposed by the MCP server (like OpenAI function calling).
- The model sees tool definitions (names, input schema, descriptions) and decides if and when to use them.

**Example:**
> User says: "Send a calendar invite for tomorrow at 2pm."  
> → Model calls `create_calendar_event()`.

#### 📦 2. The Application (Host) — Controls **which _resources_ are included**
- Resources (e.g., documents, emails, data) are not included automatically.
- The host app determines what resources to send into the model’s context.
- Helps manage token limits and privacy.

**Example:**
> The client app injects the 5 most recent meeting transcripts into the LLM’s context.

#### 👤 3. The User — Selects **which _prompt_ to activate**
- Prompts are pre-built message templates to guide the model.
- Users can explicitly choose a prompt like:
  - “Summarize my inbox”
  - “Generate weekly report”
- These can be rendered as buttons, slash commands, or suggestions in UI.

#### ⚙️ Behind the Scenes: Discovery & Coordination

1. The client queries the MCP server for available:
   - **Tools** (functions the model can call)
   - **Resources** (data the app can feed into context)
   - **Prompts** (user-triggered templates)
2. Selected items are embedded into the LLM’s system message and prompt.
3. The model dynamically decides whether to:
   - Use a prompt,
   - Leverage context resources,
   - Call tools,
   - Or just respond with text.

#### 🧠 Example Flow

```text
User: "Create a report based on my last 5 meetings."

↓
→ Application loads 5 meeting transcripts as resources  
→ User selects the "Generate Report" prompt  
→ LLM uses prompt + resources  
→ Model calls tool: summarize_each_meeting()  
→ Model composes report from summaries  
→ Final report is returned to user


## Concepts

### 1. Tools
- **Purpose**: Executable actions (e.g., send an email, search a database, run a script).
- **Controlled by**: The **model** (LLM decides when to call).
- **Examples**: `generate_invoice`, `search_files`, `send_slack_message`.

### 2. Resources
- **Purpose**: Provide read-only contextual data to inform the model.
- **Controlled by**: The **application** (host decides what’s available in context).
- **Examples**: Documents, calendar events, database records.

### 3. Prompts
- **Purpose**: Pre-configured message templates that guide LLM behavior.
- **Controlled by**: The **user** (user explicitly chooses).
- **Examples**: "Summarize recent meetings", "Draft customer response", "Plan a vacation".

## Useful repositories

- [Spring WebMVC example project](https://github.com/spring-projects/spring-ai-examples/blob/main/model-context-protocol/weather/starter-webmvc-server/README.md)
- [Chat Client](https://docs.spring.io/spring-ai/reference/api/chatclient.html)
- [How to use prompts/ resources](https://github.com/spring-projects/spring-ai-examples/blob/5e31bbad50248f2764f9ce175fd749f7c9b60b6e/model-context-protocol/mcp-annotations-server/src/main/java/org/springframework/ai/mcp/sample/server/McpServerApplication.java)