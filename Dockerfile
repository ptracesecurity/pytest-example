# Używamy oficjalnego obrazu Pythona jako obrazu bazowego
FROM python:3.11

# Instalujemy wymagane pakiety
RUN pip install --no-cache-dir -r requirements.txt

# Uruchamiamy pytest
CMD ["pytest"]