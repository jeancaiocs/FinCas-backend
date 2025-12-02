package com.jeancaio.financecontrol.config;

import com.jeancaio.financecontrol.model.Category;
import com.jeancaio.financecontrol.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Só cria as categorias se o banco estiver vazio
        if (categoryRepository.count() == 0) {
            List<Category> categories = Arrays.asList(
                    // Categorias de RECEITA
                    createCategory("Salário", "💰", "#4CAF50"),
                    createCategory("Freelance", "💼", "#2196F3"),
                    createCategory("Investimentos", "📈", "#9C27B0"),
                    createCategory("Outros Ganhos", "💵", "#00BCD4"),

                    // Categorias de DESPESA
                    createCategory("Alimentação", "🍔", "#FF5722"),
                    createCategory("Transporte", "🚗", "#FF9800"),
                    createCategory("Moradia", "🏠", "#795548"),
                    createCategory("Saúde", "🏥", "#E91E63"),
                    createCategory("Educação", "📚", "#3F51B5"),
                    createCategory("Lazer", "🎮", "#9C27B0"),
                    createCategory("Compras", "🛒", "#F44336"),
                    createCategory("Contas", "📱", "#607D8B"),
                    createCategory("Outros Gastos", "💸", "#9E9E9E")
            );

            categoryRepository.saveAll(categories);
            System.out.println("✅ " + categories.size() + " categorias padrão criadas com sucesso!");
        } else {
            System.out.println("ℹ️ Categorias já existem no banco de dados.");
        }
    }

    private Category createCategory(String name, String icon, String color) {
        Category category = new Category();
        category.setName(name);
        category.setIcon(icon);
        category.setColor(color);
        return category;
    }
}