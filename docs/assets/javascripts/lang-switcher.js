document.addEventListener("DOMContentLoaded", function () {
    const path = window.location.pathname;
    const isEnglish = path.startsWith("/en/");

    const viUrl = isEnglish ? path.slice(3) || "/" : path;
    const enUrl = isEnglish ? path : "/en" + path;

    const btn = document.createElement("a");
    btn.href = isEnglish ? viUrl : enUrl;
    btn.className = "lang-btn active";
    btn.textContent = isEnglish ? "EN" : "VI";
    btn.title = isEnglish ? "Switch to Tiếng Việt" : "Switch to English";

    const switcher = document.createElement("div");
    switcher.className = "lang-switcher";
    switcher.appendChild(btn);

    const headerInner = document.querySelector(".md-header__inner");
    const searchBtn = document.querySelector(".md-header__button[for='__search']");

    if (headerInner && searchBtn) {
        headerInner.insertBefore(switcher, searchBtn);
    }
});