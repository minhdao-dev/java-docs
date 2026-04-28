(function () {
    function initLangSwitcher() {
        // Read the hreflang alternate links that MkDocs i18n generates in every page.
        // These are already the exact built URLs — no manual path math needed.
        var isEnglish = document.documentElement.lang === 'en';
        var targetLang = isEnglish ? 'vi' : 'en';

        var altLink = document.querySelector(
            'link[rel="alternate"][hreflang="' + targetLang + '"]'
        );
        if (!altLink) return;

        // altLink.href is resolved to an absolute URL by the browser automatically.
        var targetUrl = altLink.href;

        document.querySelectorAll('.lang-switcher').forEach(function (el) { el.remove(); });

        var btn = document.createElement('a');
        btn.href = targetUrl;
        btn.className = 'lang-btn active';
        btn.textContent = isEnglish ? 'EN' : 'VI';
        btn.title = isEnglish ? 'Switch to Tiếng Việt' : 'Switch to English';

        var switcher = document.createElement('div');
        switcher.className = 'lang-switcher';
        switcher.appendChild(btn);

        var headerInner = document.querySelector('.md-header__inner');
        var searchBtn = document.querySelector('.md-header__button[for="__search"]');

        if (headerInner && searchBtn) {
            headerInner.insertBefore(switcher, searchBtn);
        }
    }

    if (typeof document$ !== 'undefined') {
        document$.subscribe(initLangSwitcher);
    } else {
        document.addEventListener('DOMContentLoaded', initLangSwitcher);
    }
}());
