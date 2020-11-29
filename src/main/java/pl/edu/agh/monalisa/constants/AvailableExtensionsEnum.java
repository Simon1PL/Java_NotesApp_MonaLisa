package pl.edu.agh.monalisa.constants;

public class AvailableExtensionsEnum {
    public enum AvailableExtensions {
        PYTHON,
        TXT
    }

    public static AvailableExtensions getExtension(String extension) {
        if (extension.endsWith(".py")) {
            return AvailableExtensions.PYTHON;
        }
        else  {
            return AvailableExtensions.TXT;
        }
    }
}
