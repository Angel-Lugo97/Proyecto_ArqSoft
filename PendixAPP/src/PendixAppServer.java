import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class PendixAppServer {
    private static final int PORT = 5018;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        String url = "http://localhost:" + PORT;

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(HOST, PORT), 0);
            server.createContext("/", new PendixHandler());
            server.setExecutor(null);
            server.start();

            System.out.println();
            System.out.println("============================================");
            System.out.println(" PendixAPP ejecutandose correctamente");
            System.out.println(" URL: " + url);
            System.out.println(" Servidor Java en puerto " + PORT);
            System.out.println(" Presiona CTRL + C para detenerlo");
            System.out.println("============================================");
            System.out.println();

            abrirNavegador(url);
        } catch (BindException e) {
            System.out.println();
            System.out.println("El puerto " + PORT + " ya esta ocupado.");
            System.out.println("Abre directamente: " + url);
            System.out.println("O cierra el programa que esta usando ese puerto y vuelve a ejecutar.");
            System.out.println();
        } catch (IOException e) {
            System.out.println("No se pudo iniciar PendixAPP en el puerto " + PORT + ".");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    private static void abrirNavegador(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
                return;
            }
        } catch (Throwable ignored) {
            // Si el sistema no permite abrir navegador automaticamente, el servidor sigue activo.
        }

        String os = System.getProperty("os.name").toLowerCase();
        String[] comando;

        if (os.contains("win")) {
            comando = new String[]{"cmd", "/c", "start", url};
        } else if (os.contains("mac")) {
            comando = new String[]{"open", url};
        } else {
            comando = new String[]{"xdg-open", url};
        }

        try {
            Runtime.getRuntime().exec(comando);
        } catch (IOException ignored) {
            System.out.println("Abre manualmente en el navegador: " + url);
        }
    }

    private static class PendixHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();

            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                enviar(exchange, 405, "text/plain; charset=UTF-8", "Metodo no permitido");
                return;
            }

            if ("/".equals(path) || "/index.html".equals(path)) {
                enviar(exchange, 200, "text/html; charset=UTF-8", html());
                return;
            }

            if ("/api/pendientes".equals(path)) {
                enviar(exchange, 200, "application/json; charset=UTF-8", "[\"PendixAPP API simulada desde Java\"]");
                return;
            }

            enviar(exchange, 404, "text/plain; charset=UTF-8", "Archivo no encontrado");
        }

        private void enviar(HttpExchange exchange, int status, String contentType, String body) throws IOException {
            byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", contentType);
            headers.set("Cache-Control", "no-store");
            exchange.sendResponseHeaders(status, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    private static String html() {
        return """
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>PendixAPP</title>
<style>
:root{
  --bg:#080a12;--panel:#111520;--panel2:#171b28;--line:#2a3144;--text:#f6f7fb;--muted:#a9b0c5;
  --purple:#9b5cff;--purple2:#6d28d9;--violet:#c084fc;--red:#ff6b6b;--red2:#3a151c;
  --green:#45d483;--green2:#113320;--yellow:#facc15;--blue:#60a5fa;--white:#ffffff;
}
*{box-sizing:border-box}
body{margin:0;min-height:100vh;background:radial-gradient(circle at top,#2a1b4d 0,#0b0f19 42%,#05070d 100%);font-family:Arial,Helvetica,sans-serif;color:var(--text);display:flex;justify-content:center;align-items:center;padding:22px}
.stage{display:flex;gap:34px;align-items:center;max-width:1180px;width:100%;justify-content:center}
.info{max-width:360px}.eyebrow{color:var(--violet);font-weight:800;text-transform:uppercase;letter-spacing:.12em;font-size:12px}.info h1{font-size:44px;line-height:1;margin:8px 0}.info p{color:#c9cde0;font-size:17px;line-height:1.55}
.quick{margin-top:20px;padding:14px 16px;border:1px solid rgba(192,132,252,.35);border-radius:18px;background:rgba(17,21,32,.72)}.quick code{color:#d8b4fe}
.browser{width:430px;border:1px solid rgba(255,255,255,.15);border-radius:26px;background:#111722;box-shadow:0 30px 90px rgba(0,0,0,.55);overflow:hidden}.browserbar{height:46px;background:linear-gradient(180deg,#242a37,#151922);display:flex;align-items:center;justify-content:space-between;padding:0 16px;color:#d9def2;border-bottom:1px solid rgba(255,255,255,.1)}.browserbar span:nth-child(2){background:#202634;border:1px solid #32394a;border-radius:14px;padding:7px 28px;font-size:14px}
.phone{margin:16px auto;width:386px;height:760px;border-radius:44px;padding:12px;background:linear-gradient(145deg,#1d2330,#07090e);border:1px solid rgba(255,255,255,.18);box-shadow:inset 0 0 0 8px #06080d,0 12px 45px rgba(0,0,0,.5)}
.screen{height:100%;border-radius:34px;overflow:hidden;background:linear-gradient(180deg,#0f1320,#070a12);position:relative;display:flex;flex-direction:column}.content{padding:18px 16px 88px;overflow-y:auto;scrollbar-width:none;scroll-behavior:smooth}.content::-webkit-scrollbar{display:none}
.status{display:flex;justify-content:space-between;align-items:center;font-size:13px;font-weight:800;margin-bottom:14px}.icons{letter-spacing:2px}.top{display:flex;align-items:center;gap:12px;margin-bottom:16px}.logo{width:54px;height:54px;border-radius:16px;background:linear-gradient(135deg,var(--purple),var(--purple2));display:grid;place-items:center;font-size:28px;box-shadow:0 0 25px rgba(155,92,255,.35)}.title h2{margin:0;font-size:28px}.title p{margin:2px 0 0;color:var(--muted);font-size:14px}.header-actions{margin-left:auto;display:flex;gap:10px}.iconbtn{width:42px;height:42px;border-radius:50%;border:1px solid var(--line);background:rgba(255,255,255,.04);display:grid;place-items:center;position:relative;cursor:pointer;color:var(--text)}.badge{position:absolute;right:-3px;top:-4px;background:var(--red);font-size:11px;border-radius:999px;padding:3px 6px}.filters{display:grid;grid-template-columns:repeat(4,1fr);gap:8px;padding:8px;border:1px solid rgba(255,255,255,.08);border-radius:22px;background:rgba(255,255,255,.03);margin-bottom:14px}.filter{border:0;border-radius:18px;padding:10px 6px;background:#1b202c;color:var(--text);font-weight:800;cursor:pointer}.filter span{margin-left:4px;background:rgba(255,255,255,.13);border-radius:999px;padding:3px 7px;font-size:12px}.filter.active{background:linear-gradient(135deg,var(--purple),var(--purple2));box-shadow:0 0 22px rgba(155,92,255,.32)}.filter.red{color:#ff8b8b}
.card{border:1px solid rgba(255,255,255,.09);background:rgba(17,21,32,.72);border-radius:22px;padding:14px;margin-bottom:14px}.section-title{display:flex;align-items:center;gap:10px;margin:2px 0 12px}.section-title strong{font-size:21px}.section-title .link{margin-left:auto;color:var(--violet);font-weight:800;font-size:14px;cursor:pointer}.notif{display:grid;grid-template-columns:42px 1fr auto;gap:10px;align-items:center;margin:9px 0;padding:12px;border-radius:16px;background:linear-gradient(90deg,rgba(255,107,107,.22),rgba(255,107,107,.09));border:1px solid rgba(255,107,107,.3)}.warn{font-size:25px}.notif h4,.task h4{margin:0 0 4px}.notif p,.task p{margin:0;color:#c7cbda;font-size:13px}.pill{display:inline-block;border-radius:999px;padding:4px 9px;font-size:11px;font-weight:900}.expired{background:#5a1b24;color:#ff8b8b}.done{background:var(--green2);color:var(--green)}.activeP{background:#42380e;color:var(--yellow)}
.plans{display:grid;grid-template-columns:1fr 1fr 1fr;gap:8px}.plan{padding:12px;border-radius:16px;background:rgba(255,255,255,.035);border:1px solid var(--line)}.plan.pro{border-color:var(--purple);box-shadow:0 0 24px rgba(155,92,255,.18)}.plan h3{margin:0 0 8px;font-size:16px}.price{font-size:24px;font-weight:900;margin-bottom:6px}.plan ul{padding-left:0;list-style:none;margin:8px 0 12px}.plan li{font-size:12px;color:#d3d6e3;margin:6px 0}.plan li:before{content:'✓';color:var(--violet);margin-right:5px}.btn{border:1px solid var(--purple);background:transparent;color:var(--violet);border-radius:12px;padding:10px 12px;font-weight:900;cursor:pointer;width:100%}.btn.fill{background:linear-gradient(135deg,var(--purple),var(--purple2));color:white}.btn.danger{border-color:var(--red);color:#ff8b8b}.login-row{display:grid;grid-template-columns:1fr 1fr;gap:10px;margin-top:12px}
.task{display:grid;grid-template-columns:30px 1fr auto;gap:10px;align-items:center;padding:12px;border-radius:16px;background:rgba(255,255,255,.045);border:1px solid rgba(255,255,255,.08);margin:8px 0}.task.vencido{border-left:4px solid var(--red);background:linear-gradient(90deg,rgba(255,107,107,.17),rgba(255,255,255,.04))}.task.completed h4{text-decoration:line-through;color:#818899}.check{width:24px;height:24px;border-radius:50%;border:2px solid #d5daf0;display:grid;place-items:center;cursor:pointer}.completed .check{background:var(--purple);border-color:var(--purple);color:white}.actions{display:flex;gap:6px;justify-content:flex-end;align-items:center}.mini{border:0;background:rgba(255,255,255,.06);color:#e8e9f1;border-radius:9px;padding:7px 8px;cursor:pointer}.mini:hover,.iconbtn:hover,.btn:hover{filter:brightness(1.12)}.undo{color:var(--violet);font-weight:900;font-size:13px;background:transparent;border:0;cursor:pointer}
.view{display:none}.view.active-view{display:block}.calendar-day{padding:12px;border-radius:16px;margin:8px 0;background:rgba(255,255,255,.045);border:1px solid rgba(255,255,255,.08)}.calendar-day strong{display:block;color:#d8b4fe;margin-bottom:6px}.setting-row{display:flex;justify-content:space-between;align-items:center;gap:12px;padding:12px;border-radius:16px;margin:8px 0;background:rgba(255,255,255,.045);border:1px solid rgba(255,255,255,.08)}.toggle{width:52px;height:28px;border-radius:99px;background:var(--purple);position:relative;border:0}.toggle:after{content:'';width:22px;height:22px;border-radius:50%;background:white;position:absolute;right:3px;top:3px}.field{width:100%;padding:11px 12px;border-radius:12px;border:1px solid var(--line);background:#0d111b;color:var(--text);outline:0;margin:6px 0}.field:focus{border-color:var(--purple)}.form-grid{display:grid;grid-template-columns:1fr 1fr;gap:8px}.full{grid-column:1/-1}.muted{color:var(--muted)}
.bottom{position:absolute;left:0;right:0;bottom:0;height:74px;background:rgba(8,10,18,.94);border-top:1px solid rgba(255,255,255,.08);display:grid;grid-template-columns:repeat(4,1fr);align-items:center}.nav{font-size:11px;text-align:center;color:#d7d9e7;border:0;background:transparent;cursor:pointer}.nav.active{color:var(--violet);font-weight:900}.nav b{display:block;font-size:22px;margin-bottom:2px}.add{margin-left:auto;color:var(--violet);font-weight:900;cursor:pointer}
.modal{position:absolute;inset:0;z-index:20;background:rgba(0,0,0,.65);display:none;align-items:center;justify-content:center;padding:18px}.modal.open{display:flex}.modal-box{width:100%;max-width:330px;background:#111520;border:1px solid rgba(192,132,252,.35);border-radius:22px;padding:16px;box-shadow:0 20px 70px rgba(0,0,0,.55)}.modal-head{display:flex;justify-content:space-between;align-items:center;margin-bottom:10px}.modal-head h3{margin:0}.close{background:transparent;color:#fff;border:0;font-size:22px;cursor:pointer}.toast{position:absolute;left:18px;right:18px;bottom:84px;background:#191f2d;border:1px solid rgba(192,132,252,.45);color:#fff;border-radius:14px;padding:12px;text-align:center;display:none;z-index:30}.toast.show{display:block;animation:fade 2.2s forwards}@keyframes fade{0%{opacity:0;transform:translateY(8px)}15%{opacity:1;transform:translateY(0)}80%{opacity:1}100%{opacity:0}}
@media(max-width:900px){body{padding:10px}.stage{display:block}.info{display:none}.browser{width:100%;max-width:430px;margin:auto}.phone{width:calc(100% - 24px);max-width:386px}}
</style>
</head>
<body>
<div class="stage">
  <section class="info">
    <div class="eyebrow">Servidor Java</div>
    <h1>PendixAPP</h1>
    <p>Prototipo web con apariencia de celular</p>
  </section>

  <main class="browser">
    <div class="browserbar"><span>AA</span><span>localhost:5018</span><span>↻</span></div>
    <section class="phone" aria-label="PendixAPP en vista celular">
      <div class="screen">
        <div class="content" id="content">
          <div class="status"><span>9:41</span><span class="icons">🔋</span></div>
          <header class="top">
            <div class="logo">☑</div>
            <div class="title"><h2>PendixAPP</h2><p id="sessionText">Tus pendientes, bajo control</p></div>
            <div class="header-actions"><button class="iconbtn" onclick="showView('recordatorios')">🔔<span class="badge" id="notifBadge">0</span></button><button class="iconbtn" onclick="openLogin()">👤</button></div>
          </header>

          <section id="viewPendientes" class="view active-view">
            <nav class="filters" id="filters"></nav>
            <section class="card" id="notificacionesCard">
              <div class="section-title"><span>🔔</span><strong>Notificaciones</strong><span class="link" onclick="showView('recordatorios')">Ver todas ›</span></div>
              <div id="notificacionesHome"></div>
            </section>
            <section class="card">
              <div class="section-title"><span>⭐</span><strong>Planes / Iniciar sesión</strong></div>
              <p class="muted" style="margin-top:-6px">Elige el plan ideal para ti</p>
              <div class="plans">
                <article class="plan"><h3>Gratis</h3><div class="price">$0</div><ul><li>Pendientes ilimitados</li><li>Recordatorios básicos</li><li>Notificaciones locales</li></ul><button class="btn" onclick="selectPlan('Gratis')">Comenzar</button></article>
                <article class="plan pro"><h3>Pro</h3><div class="price">$4.99</div><ul><li>Todo lo de Gratis</li><li>Recordatorios avanzados</li><li>Exportación</li></ul><button class="btn fill" onclick="selectPlan('Pro')">Probar Pro</button></article>
                <article class="plan"><h3>Premium</h3><div class="price">$9.99</div><ul><li>Todo lo de Pro</li><li>Backups</li><li>Etiquetas</li></ul><button class="btn" onclick="selectPlan('Premium')">Premium</button></article>
              </div>
              <div class="login-row"><button class="btn" onclick="openLogin()">↪ Iniciar sesión</button><button class="btn fill" onclick="openLogin(true)">👤 Crear cuenta</button></div>
            </section>
            <section class="card" id="tasksSection">
              <div class="section-title"><span>📋</span><strong>Mis pendientes</strong><span class="add" onclick="openTaskForm()">+ Nuevo pendiente</span></div>
              <div id="tasks"></div>
            </section>
          </section>

          <section id="viewCalendario" class="view">
            <section class="card">
              <div class="section-title"><span>📅</span><strong>Calendario</strong><span class="link" onclick="showView('pendientes')">Volver</span></div>
              <p class="muted">Vista rápida de pendientes organizados por fecha.</p>
              <div id="calendarList"></div>
            </section>
          </section>

          <section id="viewRecordatorios" class="view">
            <section class="card">
              <div class="section-title"><span>🔔</span><strong>Recordatorios</strong><span class="link" onclick="showView('pendientes')">Volver</span></div>
              <p class="muted">Recordatorios vencidos y activos generados desde tus pendientes.</p>
              <div id="reminderList"></div>
            </section>
          </section>

          <section id="viewAjustes" class="view">
            <section class="card">
              <div class="section-title"><span>⚙</span><strong>Ajustes</strong><span class="link" onclick="showView('pendientes')">Volver</span></div>
              <div class="setting-row"><div><strong>Tema oscuro</strong><p class="muted">Activo para reducir fatiga visual.</p></div><button class="toggle" onclick="toast('El tema oscuro ya está activo')"></button></div>
              <div class="setting-row"><div><strong>Notificaciones locales</strong><p class="muted">Recordatorios simulados en la maqueta.</p></div><button class="toggle" onclick="toast('Notificaciones locales activas')"></button></div>
              <button class="btn" onclick="exportData()">Exportar pendientes</button><br><br>
              <button class="btn danger" onclick="resetData()">Restaurar datos de ejemplo</button><br><br>
              <button class="btn" onclick="logout()">Cerrar sesión</button>
            </section>
          </section>
        </div>

        <footer class="bottom">
          <button class="nav active" data-nav="pendientes" onclick="showView('pendientes')"><b>▣</b>Pendientes</button>
          <button class="nav" data-nav="calendario" onclick="showView('calendario')"><b>▤</b>Calendario</button>
          <button class="nav" data-nav="recordatorios" onclick="showView('recordatorios')"><b>♢</b>Recordatorios</button>
          <button class="nav" data-nav="ajustes" onclick="showView('ajustes')"><b>⚙</b>Ajustes</button>
        </footer>

        <div class="modal" id="taskModal">
          <div class="modal-box">
            <div class="modal-head"><h3 id="taskModalTitle">Nuevo pendiente</h3><button class="close" onclick="closeTaskForm()">×</button></div>
            <input type="hidden" id="taskId">
            <input class="field" id="taskTitle" placeholder="Título del pendiente">
            <div class="form-grid">
              <input class="field" id="taskDate" type="date">
              <input class="field" id="taskTime" type="time">
              <input class="field" id="taskCategory" placeholder="Categoría">
              <select class="field" id="taskState"><option value="activo">Activo</option><option value="vencido">Vencido</option><option value="completado">Completado</option></select>
            </div>
            <button class="btn fill" onclick="saveTask()">Guardar pendiente</button>
          </div>
        </div>

        <div class="modal" id="loginModal">
          <div class="modal-box">
            <div class="modal-head"><h3 id="loginTitle">Iniciar sesión</h3><button class="close" onclick="closeLogin()">×</button></div>
            <input class="field" id="loginName" placeholder="Nombre de usuario">
            <input class="field" id="loginEmail" type="email" placeholder="Correo electrónico">
            <input class="field" id="loginPass" type="password" placeholder="Contraseña">
            <button class="btn fill" onclick="login()">Continuar</button>
          </div>
        </div>

        <div class="toast" id="toast"></div>
      </div>
    </section>
  </main>
</div>

<script>
const tareasBase=[
 {id:1,t:'Preparar presentación',fecha:'Hoy, 2:00 PM',cat:'Trabajo',estado:'vencido'},
 {id:2,t:'Comprar víveres',fecha:'Mañana, 9:00 AM',cat:'Personal',estado:'activo'},
 {id:3,t:'Enviar reporte semanal',fecha:'Hoy, 9:00 AM',cat:'Trabajo',estado:'completado'},
 {id:4,t:'Revisar tarea de arquitectura',fecha:'Hoy, 8:00 PM',cat:'Escuela',estado:'activo'},
 {id:5,t:'Pagar servicio',fecha:'Hoy, 8:00 AM',cat:'Casa',estado:'vencido'},
 {id:6,t:'Llamar al médico',fecha:'Ayer, 10:00 PM',cat:'Salud',estado:'vencido'},
 {id:7,t:'Leer apuntes de Java',fecha:'Viernes, 6:00 PM',cat:'Escuela',estado:'activo'},
 {id:8,t:'Ordenar documentos',fecha:'Ayer, 3:00 PM',cat:'Personal',estado:'completado'}
];
let tareas=JSON.parse(localStorage.getItem('pendixapp_tareas_java5018')||'null')||tareasBase;
let filtro='todos';
let usuario=JSON.parse(localStorage.getItem('pendixapp_usuario')||'null');
const filtros=document.getElementById('filters');
const tasks=document.getElementById('tasks');
const content=document.getElementById('content');

function guardar(){localStorage.setItem('pendixapp_tareas_java5018',JSON.stringify(tareas));}
function conteos(){return{todos:tareas.length,activo:tareas.filter(x=>x.estado==='activo').length,completado:tareas.filter(x=>x.estado==='completado').length,vencido:tareas.filter(x=>x.estado==='vencido').length};}
function renderFiltros(){const c=conteos();const data=[['todos','Todos',c.todos],['activo','Activos',c.activo],['completado','Listos',c.completado],['vencido','Vencidos',c.vencido]];filtros.innerHTML=data.map(x=>`<button class="filter ${filtro===x[0]?'active':''} ${x[0]==='vencido'?'red':''}" onclick="setFiltro('${x[0]}')">${x[1]} <span>${x[2]}</span></button>`).join('');document.getElementById('notifBadge').textContent=c.vencido;}
function renderTasks(){let lista=filtro==='todos'?tareas:tareas.filter(x=>x.estado===filtro);tasks.innerHTML=lista.map(x=>{let cls=x.estado==='completado'?'completed':x.estado==='vencido'?'vencido':'';let label=x.estado==='completado'?'<span class="pill done">COMPLETADO</span>':x.estado==='vencido'?'<span class="pill expired">VENCIDO</span>':'<span class="pill activeP">ACTIVO</span>';let action=x.estado==='completado'?`<button class="undo" onclick="deshacer(${x.id})">↩ Deshacer</button>`:`<button class="mini" onclick="completar(${x.id})">✓</button>`;return `<article class="task ${cls}"><div class="check" onclick="completar(${x.id})">${x.estado==='completado'?'✓':''}</div><div><h4>${x.t}</h4><p>📅 ${x.fecha} · 🏳 ${x.cat}</p></div><div style="text-align:right">${label}<br><br><div class="actions">${action}<button class="mini" onclick="editTask(${x.id})">✎</button><button class="mini" onclick="deleteTask(${x.id})">🗑</button></div></div></article>`}).join('')||'<p class="muted">No hay pendientes en este filtro.</p>';}
function renderNotificaciones(){const vencidas=tareas.filter(x=>x.estado==='vencido');const html=vencidas.slice(0,3).map(x=>`<div class="notif"><span class="warn">⚠</span><div><h4>${x.t} <span class="pill expired">VENCIDA</span></h4><p>Programada para ${x.fecha}</p></div><strong style="color:#ff8b8b">Atrasada ›</strong></div>`).join('')||'<p class="muted">No tienes notificaciones vencidas.</p>';document.getElementById('notificacionesHome').innerHTML=html;}
function renderCalendar(){const grupos={};tareas.forEach(t=>{let k=t.fecha.split(',')[0];if(!grupos[k])grupos[k]=[];grupos[k].push(t);});document.getElementById('calendarList').innerHTML=Object.keys(grupos).map(d=>`<div class="calendar-day"><strong>${d}</strong>${grupos[d].map(t=>`<p>${estadoIcon(t.estado)} ${t.t} — ${t.fecha}</p>`).join('')}</div>`).join('');}
function renderRecordatorios(){document.getElementById('reminderList').innerHTML=tareas.map(t=>`<div class="notif"><span class="warn">${t.estado==='vencido'?'⚠':'🔔'}</span><div><h4>${t.t} ${badge(t.estado)}</h4><p>${t.fecha} · ${t.cat}</p></div><strong>${t.estado==='vencido'?'Revisar':'Activo'} ›</strong></div>`).join('');}
function badge(e){return e==='completado'?'<span class="pill done">COMPLETADO</span>':e==='vencido'?'<span class="pill expired">VENCIDA</span>':'<span class="pill activeP">ACTIVO</span>';}
function estadoIcon(e){return e==='completado'?'✅':e==='vencido'?'⚠':'🔔';}
function setFiltro(f){filtro=f;showView('pendientes',false);render();setTimeout(()=>document.getElementById('tasksSection').scrollIntoView({behavior:'smooth',block:'start'}),70);}
function completar(id){tareas=tareas.map(x=>x.id===id?{...x,estado:'completado'}:x);guardar();render();toast('Pendiente marcado como completado');}
function deshacer(id){tareas=tareas.map(x=>x.id===id?{...x,estado:'activo'}:x);guardar();render();toast('Pendiente regresado a activo');}
function deleteTask(id){const item=tareas.find(x=>x.id===id);if(confirm('¿Eliminar "'+item.t+'"?')){tareas=tareas.filter(x=>x.id!==id);guardar();render();toast('Pendiente eliminado');}}
function openTaskForm(){document.getElementById('taskModalTitle').textContent='Nuevo pendiente';document.getElementById('taskId').value='';document.getElementById('taskTitle').value='';document.getElementById('taskDate').value='';document.getElementById('taskTime').value='';document.getElementById('taskCategory').value='Personal';document.getElementById('taskState').value='activo';document.getElementById('taskModal').classList.add('open');}
function editTask(id){const t=tareas.find(x=>x.id===id);document.getElementById('taskModalTitle').textContent='Modificar pendiente';document.getElementById('taskId').value=t.id;document.getElementById('taskTitle').value=t.t;document.getElementById('taskCategory').value=t.cat;document.getElementById('taskState').value=t.estado;document.getElementById('taskDate').value='';document.getElementById('taskTime').value='';document.getElementById('taskModal').classList.add('open');}
function closeTaskForm(){document.getElementById('taskModal').classList.remove('open');}
function saveTask(){const id=document.getElementById('taskId').value;const title=document.getElementById('taskTitle').value.trim();const cat=document.getElementById('taskCategory').value.trim()||'Personal';const estado=document.getElementById('taskState').value;const date=document.getElementById('taskDate').value;const time=document.getElementById('taskTime').value;let fecha='Sin fecha';if(date||time){fecha=(date||'Pendiente')+(time?', '+time:'');}if(!title){toast('Escribe el título del pendiente');return;}if(id){tareas=tareas.map(x=>x.id==id?{...x,t:title,cat,estado,fecha:fecha==='Sin fecha'?x.fecha:fecha}:x);toast('Pendiente modificado');}else{tareas.unshift({id:Date.now(),t:title,fecha,cat,estado});toast('Pendiente agregado');}guardar();closeTaskForm();render();setFiltro('todos');}
function showView(view,scrollTop=true){document.querySelectorAll('.view').forEach(v=>v.classList.remove('active-view'));document.getElementById('view'+cap(view)).classList.add('active-view');document.querySelectorAll('.nav').forEach(n=>n.classList.toggle('active',n.dataset.nav===view));if(view==='calendario')renderCalendar();if(view==='recordatorios')renderRecordatorios();if(scrollTop)content.scrollTo({top:0,behavior:'smooth'});}
function cap(s){return s.charAt(0).toUpperCase()+s.slice(1);}
function openLogin(crear=false){document.getElementById('loginTitle').textContent=crear?'Crear cuenta':'Iniciar sesión';document.getElementById('loginModal').classList.add('open');}
function closeLogin(){document.getElementById('loginModal').classList.remove('open');}
function login(){const name=document.getElementById('loginName').value.trim()||'Usuario';const email=document.getElementById('loginEmail').value.trim()||'usuario@pendix.app';usuario={name,email};localStorage.setItem('pendixapp_usuario',JSON.stringify(usuario));closeLogin();renderSession();toast('Sesión iniciada como '+name);}
function logout(){usuario=null;localStorage.removeItem('pendixapp_usuario');renderSession();toast('Sesión cerrada');}
function renderSession(){document.getElementById('sessionText').textContent=usuario?'Sesión: '+usuario.name:'Tus pendientes, bajo control';}
function selectPlan(p){toast('Plan seleccionado: '+p);openLogin();}
function exportData(){navigator.clipboard?.writeText(JSON.stringify(tareas,null,2));toast('Datos copiados al portapapeles');}
function resetData(){if(confirm('¿Restaurar los datos de ejemplo?')){tareas=[...tareasBase];guardar();filtro='todos';render();toast('Datos restaurados');}}
function toast(msg){const t=document.getElementById('toast');t.textContent=msg;t.classList.remove('show');void t.offsetWidth;t.classList.add('show');}
function render(){renderFiltros();renderTasks();renderNotificaciones();renderCalendar();renderRecordatorios();renderSession();}
render();
</script>
</body>
</html>
""";
    }
}
