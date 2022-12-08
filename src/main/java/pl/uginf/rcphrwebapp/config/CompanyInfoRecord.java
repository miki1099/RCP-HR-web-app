package pl.uginf.rcphrwebapp.config;

public record CompanyInfoRecord(String name, String street, String address, String nip, String currency) {

    @Override
    public String toString() {
        String newLine = System.lineSeparator();
        return name + newLine + street + newLine + address + newLine + "NIP: " + nip;
    }
}
