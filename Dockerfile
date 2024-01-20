# Używamy oficjalnego obrazu Pythona jako obrazu bazowego
FROM python:3.11

# Ustawiamy katalog roboczy w kontenerze
WORKDIR /usr/src/app

# Kopiujemy plik requirements.txt do katalogu roboczego
COPY requirements.txt ./

# Instalujemy wymagane pakiety
RUN pip install --no-cache-dir -r requirements.txt

# Kopiujemy wszystkie pliki z bieżącego katalogu do katalogu roboczego
COPY . .

# Uruchamiamy pytest
CMD ["pytest"]