# AI usage log

| Date | Tool | Scope | Files | Human review |
|---|---|---|---|---|
| 2026-10-18 | Claude Code | Fix build break after HU-009 (missing RutasView), route service tests | `view/RutasView.java`, `RutaServiceTest.java` | Daniel Quiaro: ran app, verified create/edit/toggle routes |
| 2026-10-18 | Claude Code | Wallet test fixes (HU-013/014/015) | `AuthService.java`, `AuthServiceTest.java`, `User.java` | Daniel Quiaro: reviewed diff, tests green |
| 2026-10-18 | Claude Code | Integration of JSON persistence branch, Gson migration, removal of hardcoded super admin passwords | `database/*`, `AuthService.java`, `pom.xml` | Daniel Quiaro: reviewed security change, tests green |
| 2026-10-18 | Claude Code | Super Admin role management, role-less registration, Figma UI alignment (contrast, itinerary dropdowns/date pickers, Pago Móvil reference) | `UserRole.java`, `UsuariosView.java`, `RegisterView.java`, `ItineraryView.java`, `Estilos.java`, `PassengerDashboardView.java` | Daniel Quiaro: compared against Figma, E2E run on screen |
| 2026-10-18 | Claude Code | Solidarity fares HU-011/HU-012 | `TarifaService.java`, `TarifaDatabase.java`, `TarifasView.java`, `AuthService.java` | Daniel Quiaro: E2E run on screen, unit tests green |
