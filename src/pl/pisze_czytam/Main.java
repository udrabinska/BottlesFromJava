package pl.pisze_czytam;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class Main {
    private static ArrayList<Butelka> zbiorButelek = new ArrayList<Butelka>();

    static class Butelka {
        private double pojemnosc;
        private double wypelnienie;
        private static int numerButelki = 0;

        Butelka(double pojemnosc, double wypelnienie) {
            if (wypelnienie > pojemnosc) {
                System.out.println("Nie możesz wlać do butelki więcej, niż wynosi jej pojemność. Wypełniam butelkę, reszta płynu przepada.");
                wypelnienie = pojemnosc;
            }
            this.pojemnosc = pojemnosc;
            this.wypelnienie = wypelnienie;
            numerButelki++;
        }

        double getPojemnosc() {
            return pojemnosc;
        }

        double getWypelnienie() {
            return wypelnienie;
        }

        int getNumerButelki() {
            return numerButelki;
        }

        void wlej(double wlewane) {
            if (wlewane < 0) {
                System.out.println("Nie możesz wlać mniej niż zero litrów. Jeśli chcesz coś wylać z butelki, użyj funkcji \"wylej\".");
                return;
            }
            if (wlewane + wypelnienie <= pojemnosc) {
                wypelnienie += wlewane;
                double zostalo = pojemnosc - wypelnienie;
                System.out.println("Wlano " + wlewane + " l do butelki " + getNumerButelki() + ". Jest w niej teraz " + wypelnienie + " l. Możesz dolać jeszcze " + zostalo + " l do " + pojemnosc + " l.");
            } else {
                System.out.println("Nie można wlać aż tyle do butelki " + getNumerButelki() + ". Dolano " + (pojemnosc - wypelnienie) + " l do pełna, czyli do " + pojemnosc + " l.");
                wypelnienie = pojemnosc;
            }
        }

        void wylej(double wylewane) {
            if (wylewane < 0) {
                System.out.println("Nie możesz wylać mniej niż zero litrów. Jeśli chcesz  wlać coś do butelki, użyj funkcji \"wlej\".");
                return;
            }
            if (wylewane > wypelnienie) {
                System.out.println("Nie można wylać " + wylewane + " l. Wylewam całą zawartość butelki, czyli " + wypelnienie + " l.");
                wypelnienie = 0;
            } else {
                wypelnienie -= wylewane;
                System.out.println("Wylano " + wylewane + " l. W butelce zostało " + wypelnienie + " l.");
            }
        }

        // TODO: do sprawdzenia, czy nie trzeba rozwinąć / zmienić info dla użytkownika / zmienić samą funkcję
        void przelej(Butelka butelkaZ, Butelka butelkaDo, double przelewane) {
            butelkaZ.wylej(przelewane);
            butelkaDo.wlej(przelewane);
        }
    }

// TODO: docinać wszystkie obliczenia na double do dwóch miejsc po przecinku
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Witaj w generatorze butelek! Ten prosty program służy do ich tworzenia, wlewania do nich i wylewania z nich wody (bądź innego płynu), przelewania płynu pomiędzy butelkami." +
                "\nZa każdym razem, gdy będziesz chciała bądź chciał przejść dalej, wciśnij enter.");
        scanner.nextLine();
        System.out.println("Stwórzmy na początek pierwszą butelkę.");
        scanner.nextLine();
        stworzButelke(scanner);
        System.out.println("Dziękujemy za zabawę butelkami. Do widzenia!");
        scanner.close();
    }

    private static void spytajCoDalej(Scanner scanner) {
        System.out.println("Co chcesz zrobić teraz? Wpisz odpowiednią cyfrę.");
        System.out.println("1 - Stwórz jeszcze jedną butelkę.");
        System.out.println("2 - Sprawdź pojemność lub zawartość wybranej butelki.");
        System.out.println("3 - Wlej coś do butelki.");
        System.out.println("4 - Wylej coś z butelki.");
        System.out.println("5 - Przelej coś z jednej butelki do drugiej.");
        System.out.println("6 - Zobacz zestawienie wszystkich swoich butelek.");
        if (scanner.hasNextInt()) {
            int komenda = scanner.nextInt();
            // TODO: switch na poszczególne komendy (4 do napisania; 5 do sprawdzenia) + do uporządkowanie do metod, gdy liczba spoza zakresu -> nieprawidłowa komenda, powtórz
            switch (komenda) {
                case 1:
                    stworzButelke(scanner);
                    break;
                case 2:
                    sprawdzButelke(scanner);
                    break;
                case 3:
                    int nrButelki = 0;
                    double wlewane = 0.0;
                    do {
                        System.out.println("Do której butelki chcesz coś wlać? Wpisz jej numer.");
                        if (scanner.hasNextInt()) {
                            nrButelki = scanner.nextInt();
                        } else {
                            System.out.println("Nie wiem, o którą butelkę chodzi.");
                        }
                    } while (nrButelki == 0);
                    scanner.nextLine();

                    do {
                        System.out.println("Ile chcesz wlać do butelki " + nrButelki + "?");
                        if (scanner.hasNextDouble()) {
                            wlewane = scanner.nextDouble();
                        } else {
                            System.out.println("Nie mogę odczytać, ile chcesz wlać.");
                        }
                    } while (wlewane == 0.0);
                    zbiorButelek.get(nrButelki - 1).wlej(wlewane);
                    przejdzDalej(scanner);
                    break;
                case 5:
                    switch (zbiorButelek.size()) {
                        case 0:
                            System.out.println("Nie wiem, skąd i dokąd chcesz przelewać płyn - nie masz ani jednej butelki.");
                            break;
                        case 1:
                            System.out.println("Nic nie przelejesz - masz tylko jedną butelkę. \nMożesz spróbować coś do niej wlać lub z niej wylać.");
                            break;
                        default:
                            przelej(scanner);
                    }
                    break;
                case 6:
                    for (int i = 0; i < zbiorButelek.size(); i++) {
                        wydrukujInfoButelki(i);
                        System.out.println("-------------------------");
                    }
                    przejdzDalej(scanner);
                    break;
                default:
                    System.out.println("Nie wiem, co robić. Wyraź się jaśniej.");
                    przejdzDalej(scanner);
            }
        } else {
            System.out.println("Nieprawidłowa komenda.");
            scanner.next();
            spytajCoDalej(scanner);
        }
    }

    private static void stworzButelke(Scanner scanner) {
        double pojemnosc = -1;
        do {
            System.out.println("Aby stworzyć butelkę, podaj jej pojemność.");
            try {
                pojemnosc = scanner.nextDouble();
                if (pojemnosc < 0) {
                    System.out.println("Butelka nie może mieć pojemności mniejszej niż 0.");
                }
            } catch (InputMismatchException e) {
                System.out.println("W zapisie pojemności pojawił się jakiś niepożądany znak. Naciśnij enter i spróbuj jeszcze raz.");
                scanner.nextLine();
                scanner.nextLine();
            }
        } while (pojemnosc <= 0);
        scanner.nextLine();

        double wypelnienie = -1;
        do {
            System.out.println("Ile litrów chcesz do niej wlać?");
            try {
                wypelnienie = scanner.nextDouble();
                if (wypelnienie < 0) {
                    System.out.println("Nie możesz wlać mniej niż zero litrów. Butelka będzie pusta, dopóki w nią czegoś nie wlejesz.");
                    wypelnienie = 0;
                } else if (wypelnienie > pojemnosc) {
                    wypelnienie = pojemnosc;
                    System.out.println("Od razu chcesz przelewać? Dopełniam butelkę do " + pojemnosc + " l, a resztę płynu tracisz.");
                }
            } catch (InputMismatchException e) {
                System.out.println("W wartości pojawił się jakiś niepożądany znak. Naciśnij enter i spróbuj jeszcze raz.");
                scanner.nextLine();
                scanner.nextLine();
            }
        } while (wypelnienie < 0);

        Butelka butelka = new Butelka(pojemnosc, wypelnienie);
        System.out.println("Stworzyłeś pierwszą butelkę o pojemności " + pojemnosc + " l z " + wypelnienie + " l płynu. "
                + "Będziesz mógł dolać jeszcze " + (pojemnosc - wypelnienie) + " l.");
        System.out.println("Gdy będziesz chciał(a) potem z niej skorzystać, przy wyborze butelki wpisz " + butelka.getNumerButelki() + ".");
        zbiorButelek.add(butelka);
        przejdzDalej(scanner);
    }

    private static void sprawdzButelke(Scanner scanner) {
        if (zbiorButelek.size() == 0) {
            System.out.println("Nie masz żadnych butelek. Nie wiem, co chcesz sprawdzać.");
            przejdzDalej(scanner);
        } else if (zbiorButelek.size() == 1) {
            System.out.println("Masz jedną butelkę. Jej pojemność to: " + zbiorButelek.get(0).getPojemnosc() + " l i jest w niej " + zbiorButelek.get(0).getWypelnienie() + " l płynu.");
            przejdzDalej(scanner);
        } else {
            System.out.println("Której butelki pojemność bądź zawartość chcesz sprawdzić? Wpisz numer od 1 do " + zbiorButelek.size() + ".");
            if (scanner.hasNextInt()) {
                int nrButelki = scanner.nextInt();
                if (nrButelki <= 0 || nrButelki > zbiorButelek.size()) {
                    System.out.println("Nie wiem, o którą butelkę Ci chodzi. Żadna butelka z dostępnych nie ma takiego numeru.");
                } else {
                    // TODO: pytanie o to, czego chce się dowiedzieć użytkownik (pojemność, wypełnienie, ile można dolać czy chce wiedzieć wszystko
                    // TODO cd: + być może rozbicie metody "wydrukujInfoButelki" - w jej ramach poszczególne metody drukujące jeden wers)
                    int indeksButelki = nrButelki - 1;
                    wydrukujInfoButelki(indeksButelki);
                    przejdzDalej(scanner);
                }
            } else {
                // TODO: jeśli nie int, a np. double lub struna.
            }
        }
    }

    private static void wydrukujInfoButelki(int indeks) {
        System.out.println("BUTELKA " + (indeks + 1));
        System.out.println("Pojemność: " + zbiorButelek.get(indeks).getPojemnosc() + " l");
        System.out.println("Aktualna zawartość: " + zbiorButelek.get(indeks).getWypelnienie() + " l");
        System.out.println("Możesz jeszcze dolać: " + (zbiorButelek.get(indeks).getPojemnosc() - zbiorButelek.get(indeks).getWypelnienie()) + " l");
    }

    private static void przelej(Scanner scanner) {
        System.out.println("Z której butelki chcesz coś przelać?");
        // TODO funkcja ktoraButelka - zwrotka obiekt Butelki
        String butelkaZniej = scanner.nextLine();
        int numerButelki1 = 0;
        if (butelkaZniej.contains("butelka ")) {
            String[] tnijButelke = butelkaZniej.split("butelka ");
            numerButelki1 = Integer.parseInt(tnijButelke[1]);
        } else {
            //TODO: numer zamiast komendy
            System.out.println("Aby wybrać butelkę, wpisz poprawną komendę, \"butelka + numer\", np. \"butelka 2\".");
            // TODO: powtarzaj póki ktoś nie wpisze dobrze / daj możliwość wyjścia z komendy.
        }
        if (numerButelki1 >= zbiorButelek.size()) {
            System.out.println("Nie ma takiej butelki.");
            // TODO: powtórz pytanie o numer butelki, ewentualnie daj możliwość wyjścia.
        }
        Butelka butelkaZ = zbiorButelek.get(numerButelki1 - 1);

        System.out.println("Ile chcesz przelać?");
        double przelewane = 0.0;
        if (scanner.hasNextDouble()) {
            przelewane = scanner.nextDouble();
        }
        // TODO: drugi raz funkcja ktora butelka - zwrotka butelka "do"
        // TODO: dokończyć przelewanie.
    }
    private static void przejdzDalej(Scanner scanner) {
        scanner.nextLine();
        scanner.nextLine();
        spytajCoDalej(scanner);
    }
}
